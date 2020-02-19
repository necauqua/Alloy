#!/bin/bash

VERSION="v5.1.0"

if ! which curl &> /dev/null; then
    echo "This script requires cURL to be installed and in PATH" 1>&2 
    exit 1
fi

echo "Getting the Alloy $VERSION jar:"
curl "https://github.com/AlloyTools/org.alloytools.alloy/releases/download/$VERSION/org.alloytools.alloy.dist.jar" -Lo alloy.jar --progress-bar

echo "Getting source:"
curl "https://github.com/AlloyTools/org.alloytools.alloy/archive/$VERSION.zip" -Lo alloy-src.zip --progress-bar
