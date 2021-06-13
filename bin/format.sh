#!/bin/sh
# set -e

# read 'GOOGLE_JAVA_FORMATTER_JAR' from env
. ./.env

java --add-exports jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED \
    --add-exports jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED \
    --add-exports jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED \
    --add-exports jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED \
    --add-exports jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED \
    -jar $GOOGLE_JAVA_FORMATTER_JAR --set-exit-if-changed -a -r $(find . -type f -name "*.java")

retVal=$?
if [ $retVal -ne 0 ]; then
    echo "[ERROR]: Some files needed formatting"
    exit $retVal
fi
exit $retVal