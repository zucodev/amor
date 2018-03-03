#!/bin/bash

ABSOLUTE_PATH=$(cd `dirname "${BASH_SOURCE[0]}"` && pwd)
cd $ABSOLUTE_PATH

rm -rf build

./gradlew clean test integrationTest -Dgrails.env=test -Dsc.flyway=true -Dsc.parallel=4 --stacktrace --info
