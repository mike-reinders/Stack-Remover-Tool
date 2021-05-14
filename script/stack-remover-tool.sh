#!/bin/sh

echo ""
echo ""

if [ ! -f stack-remover-tool.jar ]; then
  echo "stack-remover-tool.jar not found."
else
  java -version &>/dev/null

  if [ "${?}" -ne 0 ]; then
    echo "Java seems to be not properly installed. The JAVA_HOME/java environment variable might not be setup correctly."
  else
    java -jar stack-remover-tool.jar "$@"
  fi
fi

echo ""