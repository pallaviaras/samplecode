#!/bin/sh

echo "Running Ticket service" 

#Specify install dir
installdir=${PWD}


# Load everything in lib
libdir="${installdir}/target/lib"
for i in `ls ${libdir}/*.jar`
do
classpath=${classpath}:${i}
done
classpath=".:${classpath}:$installdir/target/config"

# force use of eastern time by setting the TZ variable
export TZ="EST5EDT"


#set JAVa home
if [[ -z $JAVA_HOME ]]; then
echo "setting JAVA_HOME to /usr/java/latest"
JAVA_HOME=/usr/java/latest
fi

$JAVA_HOME/bin/java -cp $classpath -Xms256m -Xmx1024m com.ticketservice.app.TicketServiceApplication

#Echo status of program
status=$?
echo "exit status = ${status}"

