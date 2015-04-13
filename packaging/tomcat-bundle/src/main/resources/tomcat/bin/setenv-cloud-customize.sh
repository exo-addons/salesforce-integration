#!/bin/sh
#
# Copyright (C) 2003-2013 eXo Platform SAS.
#
# This is free software; you can redistribute it and/or modify it
# under the terms of the GNU Lesser General Public License as
# published by the Free Software Foundation; either version 3 of
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
#

# -----------------------------------------------------------------------------
# *********************** Cloud Workspaces Platform ***************************
# -----------------------------------------------------------------------------
# JVM settings customisation
# -----------------------------------------------------------------------------

# Maximum Heap Size to use (Default : 2g)
[ -z "$EXO_JVM_SIZE_MAX" ] && EXO_JVM_SIZE_MAX=1400m

# Minimum Heap Size to use (Default : 512m)
[ -z "$EXO_JVM_SIZE_MIN" ] && EXO_JVM_SIZE_MIN=768m

# Size of the Permanent Generation. (Default : 256m)
[ -z "$EXO_JVM_PERMSIZE_MAX" ] && EXO_JVM_PERMSIZE_MAX=256m

# -----------------------------------------------------------------------------
# Cloud Workspaces configuration
# -----------------------------------------------------------------------------

# Master tenant name
[ -z "$TENANT_MASTERHOST" ] && TENANT_MASTERHOST="exoplatform.net"

# Master tenant repository
[ -z "$TENANT_REPOSITORY" ] && TENANT_REPOSITORY="repository_default"

# Cloud Admin Settings

[ -z "$CLOUD_BACKOFFICE_HOST" ] && CLOUD_BACKOFFICE_HOST="exoplatform.net"
[ -z "$CLOUD_BACKOFFICE_PORT" ] && CLOUD_BACKOFFICE_PORT="80"
[ -z "$CLOUD_BACKOFFICE_USERNAME" ] && CLOUD_BACKOFFICE_USERNAME="cloudadmin"
[ -z "$CLOUD_BACKOFFICE_PASSWORD" ] && CLOUD_BACKOFFICE_PASSWORD="cloudadmin"

# Database access
[ -z "$EXO_DB_HOST" ] && EXO_DB_HOST="localhost:3306"
[ -z "$EXO_DB_USER" ] && EXO_DB_USER="cloud"
[ -z "$EXO_DB_PASSWORD" ] && EXO_DB_PASSWORD="cloud"
[ -z "$EXO_DB_CONF_DIR" ] && EXO_DB_CONF_DIR="$CATALINA_HOME/gatein/conf/cloud/databases"

# JCR data for all tenants
[ -z "$EXO_TENANT_DATA_DIR" ] && EXO_TENANT_DATA_DIR="$CATALINA_HOME/gatein/data/jcr"

# JCR backup files
[ -z "$EXO_BACKUP_DIR" ] && EXO_BACKUP_DIR="$CATALINA_HOME/gatein/backup"

# Cloud Drive providers
[ -z "$GOOGLE_CLIENT_ID" ] && GOOGLE_CLIENT_ID="NO_ID"
[ -z "$GOOGLE_CLIENT_SECRET" ] && GOOGLE_CLIENT_SECRET="NO_SECRET"

# Options for Cloud App Server
EXO_CLOUD_OPTS="-Dtenant.masterhost=$TENANT_MASTERHOST \
    -Dcloud.backoffice.host=$CLOUD_BACKOFFICE_HOST \
    -Dcloud.backoffice.port=$CLOUD_BACKOFFICE_PORT \
    -Dcloud.backoffice.username=$CLOUD_BACKOFFICE_USERNAME \
    -Dcloud.backoffice.password=$CLOUD_BACKOFFICE_PASSWORD \
    -Dtenant.repository.name=$TENANT_REPOSITORY \
    -Dtenant.data.dir=$EXO_TENANT_DATA_DIR \
    -Dtenant.db.host=$EXO_DB_HOST \
    -Dtenant.db.user=$EXO_DB_USER \
    -Dtenant.db.password=$EXO_DB_PASSWORD \
    -Dtenant.db.conf.dir=$EXO_DB_CONF_DIR \
    -Dexo.backup.dir=$EXO_BACKUP_DIR \
    -Dgroovy.script.method.iteration.time=60000 \
    -Dclouddrive.google.client.id=$GOOGLE_CLIENT_ID \
    -Dclouddrive.google.client.secret=$GOOGLE_CLIENT_SECRET"
[ "$DAILYJOB_NOTIFICATION_EXPRESSION" ] && EXO_CLOUD_OPTS="$EXO_CLOUD_OPTS -DdailyJob.notification.expression=$DAILYJOB_NOTIFICATION_EXPRESSION"

# -----------------------------------------------------------------------------
# Platform configuration
# -----------------------------------------------------------------------------

# Cloud Workspaces version (will appear as Platform version)
EXO_ASSETS_VERSION=${project.version}

# Email display in "from" field of email notification. (Default: noreply@exoplatform.net)
#[ -z $EXO_EMAIL_FROM ] && EXO_EMAIL_FROM="noreply@exoplatform.net"
