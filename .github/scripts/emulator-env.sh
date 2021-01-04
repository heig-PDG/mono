#!/bin/sh

echo "#!/bin/sh"
echo "PATH=${JAVA_HOME}:\$PATH"
echo "./gradlew :web:test"
