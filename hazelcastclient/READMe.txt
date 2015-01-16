 javac -cp .:lib/hazelcast-3.1.7.jar:lib/hazelcast-client-3.1.7.jar -d target src/java/main/hazelcastClient/*
  527  cd target/
  528  jar -cvf hazelcasttestclient.jar *
  529  cp hazelcasttestclient.jar ../lib/
  530  cd ..
  531  java -cp .:lib/hazelcast-3.1.7.jar:lib/hazelcast-client-3.1.7.jar:lib/hazelcasttestclient.jar hazelcastClient.HazelcastTestClient

