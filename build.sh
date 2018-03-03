#!/bin/bash

ABSOLUTE_PATH=$(cd `dirname "${BASH_SOURCE[0]}"` && pwd)
cd $ABSOLUTE_PATH

set -e

export CACHE_CHANGING_MODULES=false

rm -rf build

./gradlew clean build assetCompile uploadAssets assemble -x test -x integrationTest -x postgresCreateDatabase --stacktrace --info --parallel -q
