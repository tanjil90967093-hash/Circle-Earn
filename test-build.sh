#!/bin/bash
export NODE_VERSION="20"
export GITHUB_ACTIONS="true"
./gradlew assembleDebug --stacktrace
