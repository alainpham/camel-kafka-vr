Exposing Services over Kafka using Camel
===========================

This Project shows how to use Camel with Kafka and taking advantage replay capabilities and manipulating offsets to replay events in reverse.

![Alt text](assets/screenshot01.png?raw=true "Title")

For testing

1. Download KAFKA
		
2. Start Zookeeper

		bin/zookeeper-server-start.sh config/zookeeper.properties
		
3. Start Kafka Broker

		bin/kafka-server-start.sh config/server.properties
	
4. Create a topic called test
		
		bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test
	
5. Run the App
    	
    	mvn spring-boot:run

6. View rest operations with swagger ui    
    	
    	http://localhost:8090/webjars/swagger-ui/2.1.0/index.html?url=/camel/api-docs#/
    
    
7. Delete Topic and recreate to start from scratch
    	
		bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic test

8. Graphical view
		http://localhost:8290/
		http://localhost:8290/consumer.html
		http://localhost:8290/topicview.html
    
    
    
