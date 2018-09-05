Camel Spring Boot Project camel-kafka-vr
===========================

This Project shows how to use Camel with Kafka and taking advantage replay capabilities and manipulating offsets to replay events in reverse.

![Alt text](assets/screenshot01.png?raw=true "Title")

For testing

	Download KAFKA
		
	Start Zookeeper
	
		bin/zookeeper-server-start.sh config/zookeeper.properties
		
	Start Kafka Broker
		
		bin/kafka-server-start.sh config/server.properties
		
	
	Create a topic called test
		
		bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test
	
	Run the App
    	
    	mvn spring-boot:run

	View rest operations with swagger ui    
    	
    	http://localhost:8090/webjars/swagger-ui/2.1.0/index.html?url=/camel/api-docs#/
    
    
    Delete Topic and recreate to start from scratch
    	bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic test
    
    
    
