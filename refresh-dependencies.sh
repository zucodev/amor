#!/usr/bin/env bash

export TERM="dumb"

set -e

ABSOLUTE_PATH=$(cd `dirname "${BASH_SOURCE[0]}"` && pwd)
cd $ABSOLUTE_PATH

rm -rf build && ./gradlew dependencies
