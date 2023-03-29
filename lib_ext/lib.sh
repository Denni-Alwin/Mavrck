echo "-------------Downlading dependancies------------"

echo " Downloading jars "

mkdir ../test-lib

cd ../test-lib

wget https://github.com/SeleniumHQ/selenium/releases/download/selenium-4.8.0/selenium-server-4.8.3.jar
wget https://repo1.maven.org/maven2/commons-io/commons-io/2.4/commons-io-2.4.jar
wget https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/json-simple/json-simple-1.1.1.jar

echo "--------- Setting up jars Completed -----------"
