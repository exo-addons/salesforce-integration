#!/bin/bash
#
# Copyright (C) 2011 eXo Platform SAS.
# 
# This is free software; you can redistribute it and/or modify it
# under the terms of the GNU Lesser General Public License as
# published by the Free Software Foundation; either version 2.1 of
# the License, or (at your option) any later version.
# 
# This software is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
# Lesser General Public License for more details.
# 
# You should have received a copy of the GNU Lesser General Public
# License along with this software; if not, write to the Free
# Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
# 02110-1301 USA, or see the FSF site: http://www.fsf.org.

# Prepare Cloud Workspaces Application Server instance for use in production.
# This script 
# * creates database 'repository_default'
# * starts the Platform with sessions of EXO_DB_HOST EXO_DB_USER and EXO_DB_PASSWORD pointing to local MySQL
# * waits for Platform start and 
# * call agent's template service to create a tenant template backup (JCR backup)
# * wait for backup done and 
# * stops the Platform server

  CONNECT_TIMEOUT=900
  USE_PROFILE_SETTINGS=false
  for ARG in "$@"
  do
     if [ "$ARG" = "-use_profile_settings" ] ; then
       USE_PROFILE_SETTINGS=true
     fi
  done

  if  ! $USE_PROFILE_SETTINGS; then
    # database address and credentials
    EXO_DB_HOST="localhost:3306"
    EXO_DB_USER="root"
    EXO_DB_PASSWORD="root"
    export EXO_DB_HOST EXO_DB_USER EXO_DB_PASSWORD

    # admin credentials
    [ -z "$CLOUD_AGENT_USERNAME" ]  && CLOUD_AGENT_USERNAME="cloudadmin"
    [ -z "$CLOUD_AGENT_PASSWORD" ]  && CLOUD_AGENT_PASSWORD="cloudadmin"

    # unset cloud variables to use defaults
    unset EXO_TENANT_DATA_DIR EXO_BACKUP_DIR TENANT_REPOSITORY TENANT_MASTERHOST
    TENANT_MASTERHOST=localhost
    export EXO_TENANT_DATA_DIR EXO_BACKUP_DIR TENANT_REPOSITORY TENANT_MASTERHOST
    export CLOUD_BACKOFFICE_HOST CLOUD_BACKOFFICE_PORT CLOUD_BACKOFFICE_USERNAME CLOUD_BACKOFFICE_PASSWORD
  fi

  export CLOUD_BACKOFFICE_HOST CLOUD_BACKOFFICE_PORT CLOUD_BACKOFFICE_USERNAME CLOUD_BACKOFFICE_PASSWORD

  # cURL helpers, first parameter it's URL of REST service
  function rest() {
    local lpath=`pwd`
    local curl_res="$lpath/curl.res"
    local status=`curl -s -S --connect-timeout $CONNECT_TIMEOUT -X $1 --output $curl_res --write-out %{http_code} -u $CLOUD_AGENT_USERNAME:$CLOUD_AGENT_PASSWORD $2`
    test -f $curl_res && local res=`cat $curl_res`
    if [ $status='200'  ] ; then
      echo $res
    else
      echo "ERROR: service $1 $2 returns status $status: $res"
      exit 1
    fi
  }

  # Check if App server Platform isn't already running
  asPid="`pwd`/temp/catalina.tmp"
  if [ -e $asPid ] && [ -n "`ps -A | grep \`cat $asPid\``" ] ; then
     # already started - exit with error
     echo "ERROR: App server already running. Stop it and run this script again."
     exit 1
  fi

  SQL='drop database if exists repository_default; create database repository_default default charset latin1 collate latin1_general_cs;'
  mysql --user=$EXO_DB_USER --password=$EXO_DB_PASSWORD --host=localhost -B -N -e "$SQL" -w > mysql.log 2>&1

  # Starting PLF
  echo "Starting App server Platform"
  ./start_eXo.sh

  echo "Waiting for Platform start"
  # Waiting for full start
  sleep 120s
  i=40
  while [ 0 -lt $i ] ; do
    echo "Sending http request"
    curl --connect-timeout $CONNECT_TIMEOUT http://localhost:8080/portal/intranet/home >& /dev/null && i=0
    i=$((i - 1))
    sleep 10s
  done
  
  # Asking to create space
  echo "Creating Default Space.."
  RES=$(rest 'POST' 'http://localhost:8080/cloud-agent/rest/cloud-agent/space-service/create-default')
  if [ $? -eq 0 ]; then
    echo "OK"
  else
    echo "Space creation failed.."
  fi
  sleep 15s

  # Asking to create template
  echo "Creating Tenant Template (JCR backup)"
  ID=$(rest 'POST' 'http://localhost:8080/cloud-agent/rest/cloud-agent/template-service/template')
  echo "Issued Tenant Template: $ID"
  sleep 15s
  
 
  # Check template OK (by length)
  IDlen=$(echo ${#ID})
  if [ $IDlen -ne 32 ] ; then
    echo "ERROR: Invalid Template ID received, exiting."
    exit 1
  fi

  hasID=""
  i=0
  timeout=240
  # wait no more 20min for a backup
  while [ -z $hasID ] && [ $i -lt $timeout ] ; do
    IDS=$(rest 'GET' 'http://localhost:8080/cloud-agent/rest/cloud-agent/template-service/template')
    hasID=`expr "$IDS" : ".*\"\($ID\)\".*"`
    i=$((i + 1))
    sleep 5s
  done

  if [ $i -eq $timeout ] ; then
    echo "WARNING! Template $ID creation was not finished in time. See app server logs for details."
    ./stop_eXo.sh -force
    echo 1
  fi

  # Ok, now we can stop it
  ./stop_eXo.sh -force

  # Cleanup the app server
  rm -rf ./logs/{*,.*}
  rm -rf ./work/{*,.*}
  rm -rf ./temp/{*,.*}
  rm -rf ./gatein/data/jta/{*,.*}
  rm -rf ./gatein/data/jcr/repository
  rm -rf ./gatein/gadgets/key.txt

  echo ""
  echo "***** App server prepared *****"
  echo "Tenant Template created: $ID"
  echo ""
  echo "Configure Admin server respectively in admin.properties file:"
  echo "cloud.admin.tenant.backup.id=$ID"
  echo $ID > ./template_id.txt
