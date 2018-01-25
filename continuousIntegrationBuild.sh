#!/usr/bin/env bash
# Work around missing crypto in openjdk7
if [ "$JAVA_HOME" = "/usr/lib/jvm/java-7-openjdk-amd64" ]; then
 sudo wget "https://bouncycastle.org/download/bcprov-ext-jdk15on-158.jar" -O "${JAVA_HOME}/jre/lib/ext/bcprov-ext-jdk15on-158.jar"
 sudo perl -pi.bak -e 's/^(security\.provider\.)([0-9]+)/$1.($2+1)/ge' /etc/java-7-openjdk/security/java.security
 echo "security.provider.1=org.bouncycastle.jce.provider.BouncyCastleProvider" | sudo tee -a /etc/java-7-openjdk/security/java.security

#- sudo perl -pi.bak -e 's/^(security\.provider\.)([0-9]+)/$1.($2+1)/ge' /etc/java-6-openjdk/security/java.security
#- echo "security.provider.1=org.bouncycastle.jce.provider.BouncyCastleProvider" | sudo tee -a /etc/java-6-openjdk/security/java.security
else
 echo $JAVA_HOME
fi