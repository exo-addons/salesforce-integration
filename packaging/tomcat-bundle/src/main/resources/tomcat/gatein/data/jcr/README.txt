Cloud Workspaces JCR data files
===============================

This folder contains dymanically changing data of JCR RepositoryService.

Metadata files
==============
NOTE: this metadata changes after each tenant creation/resuming or suspending.
* bind-references.xml - JNDI bindings currently used by JCR, remove this file in case of cleanup
* repository-configuration.xml - JCR repository configuration currently used by the app server, replace this file with platform-tenant-configuration-template.xml in case of cleanup.

Repository data folders
=======================
Each repository data lies in dedicated folder named by the repository name. All data folders should be removed for a cleanup.



