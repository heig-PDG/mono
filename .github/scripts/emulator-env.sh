#!/bin/sh

echo "#!/bin/sh"
echo "export PATH=${JAVA_HOME}:\$PATH"
echo "export JAVA_HOME=${JAVA_HOME}"
echo "export GOOGLE_SERVICE_ACCOUNT=${GOOGLE_SERVICE_ACCOUNT}"
echo "export GOOGLE_DATABASE_NAME=${GOOGLE_DATABASE_NAME}"
echo "export GOOGLE_BUCKET_NAME=${GOOGLE_BUCKET_NAME}"
echo "export FIREBASE_TOKEN=${FIREBASE_TOKEN}"
echo "./gradlew :web:test"