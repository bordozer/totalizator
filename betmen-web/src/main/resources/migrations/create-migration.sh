#!/bin/bash

migration_name=$1

if [ -z "$migration_name" ]
then
	echo "Usage: create-migration.sh <migration_name>"
	exit 1
else
	pushd $(dirname $0)
	touch "$(date +%Y%m%d%H%M%S)-$migration_name.sql"
	popd
fi