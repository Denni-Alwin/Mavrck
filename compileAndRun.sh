REPO_DIR=/home/denni-15540/xaxep/Mavrck-og/Mavrck                                                                                                                                                   

SERVER_HOME=/home/denni-15540/xaxep/Mavrck-og/lib

CLASS_PATH=$SERVER_HOME/selenium-server-4.3.0.jar
for dependecyjar in `ls $SERVER_HOME/**.jar`
        do
                CLASS_PATH=$CLASS_PATH:$dependecyjar
        done

export CLASSPATH=$CLASS_PATH

find $REPO_DIR/source -iname "*.java" > $REPO_DIR/../source.txt
javac -d $REPO_DIR/../classes @$REPO_DIR/../source.txt

echo ""
echo "##### Compiled Sucessfully #####"
echo ""

echo $CLASS_PATH
jar -cf $SERVER_HOME/mavrck.jar -C $REPO_DIR/../classes com

rm $REPO_DIR/../source.txt

java -Dserver.home=/home/denni-15540/xaxep/Mavrck-og -cp $SERVER_HOME/mavrck.jar:$CLASS_PATH com.mavrck.gridbot.server.ServerMain

echo ""
echo "##### Jar Created Sucessfully. Copying to Servers#####"
echo ""

