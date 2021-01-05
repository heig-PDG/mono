#!/bin/bash

scriptPath="../.github/scripts/run.sh"
cat > $scriptPath << EOF
#!/bin/bash
export PATH=${JAVA_HOME}:\$PATH
export JAVA_HOME=${JAVA_HOME}
export GOOGLE_SERVICE_ACCOUNT=${GOOGLE_SERVICE_ACCOUNT@Q}
export GOOGLE_DATABASE_NAME=${GOOGLE_DATABASE_NAME}
export GOOGLE_BUCKET_NAME=${GOOGLE_BUCKET_NAME}
export FIREBASE_TOKEN=${FIREBASE_TOKEN}
export FIRESTORE_EMULATOR_HOST=localhost:8080
./gradlew :web:test
EOF

chmod +x $scriptPath
