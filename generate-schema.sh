#!/usr/bin/env bash

export TERM="dumb"

set -e

ABSOLUTE_PATH=$(cd `dirname "${BASH_SOURCE[0]}"` && pwd)
cd $ABSOLUTE_PATH

rm -rf build

export DATABASE_NAME="amor_test"
export FLYWAY_ENABLED="false"
export HIBERNATE_DDL_AUTO="create"
export SHUTDOWN_ON_STARTUP="true"

printf "> \e[1;37mGenerating DB schema\e[0m\n"

printf "\nDATABASE_NAME: $DATABASE_NAME"
printf "\nHIBERNATE_DDL_AUTO: $HIBERNATE_DDL_AUTO\n"

printf "\n# \e[93mCleaning database\e[0m\n\n"

set +e
psql -h localhost -U postgres -t -c "CREATE DATABASE $DATABASE_NAME;"
set -e

psql -h localhost -U postgres -t -c "REVOKE CONNECT ON DATABASE $DATABASE_NAME FROM public;"
psql -h localhost -U postgres -t -c "ALTER DATABASE $DATABASE_NAME CONNECTION LIMIT 0;"
psql -h localhost -U postgres -t -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE pid <> pg_backend_pid() AND datname='$DATABASE_NAME';"

dropdb -h localhost -U postgres -e $DATABASE_NAME

psql -h localhost -U postgres -t -c "CREATE DATABASE $DATABASE_NAME;"

printf "\n# \e[93mRunning application\e[0m\n\n"

./gradlew bootRun

mkdir -p $ABSOLUTE_PATH/build

printf "\n# \e[93mSaving schema to build/schema.sql\e[0m\n\n"

DESTINATION_FILE=$ABSOLUTE_PATH/build/schema.sql

rm -f $DESTINATION_FILE
pg_dump -h localhost -U postgres --inserts -d $DATABASE_NAME > $DESTINATION_FILE

cat $DESTINATION_FILE
