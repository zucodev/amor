#!/bin/bash

ABSOLUTE_PATH=$(cd `dirname "${BASH_SOURCE[0]}"` && pwd)
cd $ABSOLUTE_PATH

printf "> \e[1;37mInitializing databases\e[0m\n"

printf "\n# \e[93mPreparing amor database\e[0m\n\n"

psql -h localhost -U postgres -t -c "CREATE DATABASE amor;"

set -e
$ABSOLUTE_PATH/gradlew flywayMigrate

printf "\n# \e[93mPreparing amor_test database\e[0m\n\n"

set +e
psql -h localhost -U postgres -t -c "CREATE DATABASE amor_test;"
set -e

psql -h localhost -U postgres -t -c "REVOKE CONNECT ON DATABASE amor_test FROM public;"
psql -h localhost -U postgres -t -c "ALTER DATABASE amor_test CONNECTION LIMIT 0;"
psql -h localhost -U postgres -t -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE pid <> pg_backend_pid() AND datname='amor_test';"

dropdb -h localhost -U postgres -e amor_test

psql -h localhost -U postgres -t -c "CREATE DATABASE amor_test;"

$ABSOLUTE_PATH/gradlew flywayMigrate -Dgrails.env=test
