package techlab;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.component.kafka.DefaultKafkaManualCommit;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import techlab.model.GetOffsetAtDateTimeResponse;

@Component(value = "helper")
public class Helper {

	@Value("${kafka.broker}")
	private String kafkaBroker;

	@Value("${kafka.topic}")
	private String topicName;

	@EndpointInject(uri = "controlbus:route?routeId=reverse-kafka-consumer&action=stop&async=true")
	private ProducerTemplate producer;

	private KafkaConsumer<String, String> consumer;
	private TopicPartition topicPartition;

	public Date dateFromTs(Long ts) {
		return new Date(ts);
	}

	public void decrementOffset(Exchange exchange) {
		DefaultKafkaManualCommit manual = exchange.getIn().getHeader(KafkaConstants.MANUAL_COMMIT,
				DefaultKafkaManualCommit.class);

		System.out.println("class is: " + manual.getClass().getName());

		long offset = exchange.getIn().getHeader(KafkaConstants.OFFSET, Long.class);

		if (offset < 1) {
			producer.requestBody(null);
		} else {
			manual.getConsumer().seek((TopicPartition) manual.getConsumer().assignment().toArray()[0], offset - 1);
		}

	}

	@PostConstruct
	public void init() {
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBroker);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		consumer = new KafkaConsumer<>(props);
		topicPartition = new TopicPartition(topicName, 0);
	}

	public Long endOffSet() {
		Map<TopicPartition, Long> endOffsets = consumer.endOffsets(Arrays.asList((topicPartition)));
		System.out.println("Helloooo  " + endOffsets.get(topicPartition));
		return endOffsets.get(topicPartition);
	}

	public Long reverseOffset() {
		return endOffSet() - 2;
	}

	public GetOffsetAtDateTimeResponse offsetAtTime(Date time) { 
		Map<TopicPartition, Long> criteria = new HashMap<TopicPartition, Long>();
		Map<TopicPartition, OffsetAndTimestamp> results;
		GetOffsetAtDateTimeResponse response = new GetOffsetAtDateTimeResponse();
		criteria.put(topicPartition, time.getTime());
		results = consumer.offsetsForTimes(criteria);
		System.out.println(results);
		if (results.get(topicPartition) != null) {
			response.setDateTime(new Date(results.get(topicPartition).timestamp()));
			System.out.println(response.getDateTime());
			response.setOffset(results.get(topicPartition).offset());
		}
		return response;
	}

}
