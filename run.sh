#!/bin/bash

ABSOLUTE_PATH=$(cd `dirname "${BASH_SOURCE[0]}"` && pwd)
cd $ABSOLUTE_PATH

set -e

printf "> \e[1;37mRunning amor-api\e[0m\n"

./gradlew --stop
rm -rf build

if [ -f config.sh ]; then
   printf "\n# \e[93mApplying custom config\e[0m\n\n"

   eval "$(cat config.sh)"
fi

./gradlew bootRun
