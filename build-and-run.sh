#!/bin/bash

ABSOLUTE_PATH=$(cd `dirname "${BASH_SOURCE[0]}"` && pwd)
cd $ABSOLUTE_PATH

set -e

./gradlew --stop

./refresh-dependencies.sh
./build.sh

cd build/libs

java -Dgrails.env=stage -jar app.war
