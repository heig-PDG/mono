#!/bin/sh

echo "#!/bin/sh"
echo "export PATH=${JAVA_HOME}:\$PATH"
echo "export JAVA_HOME=${JAVA_HOME}"
echo "./gradlew :web:test"
