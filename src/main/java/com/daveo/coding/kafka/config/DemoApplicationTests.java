package com.daveo.coding.kafka.config;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.daveo.coding.DemoApplication;
import com.daveo.coding.kafka.ConsumerReceiver;
import com.daveo.coding.kafka.config.ConsumerReceiverConfig;
import com.daveo.coding.kafka.config.ProducerSenderConfig;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class DemoApplicationTests {

	@Autowired
	private ProducerSenderConfig producerSenderConfig;

	@Autowired
	private ConsumerReceiverConfig consumerConfig;

	@Test
	public void testWithoutEmbedeKafka() throws Exception {

		producerSenderConfig.sender()
				.send(new ProducerRecord<Integer, String>("springKafkaDemoTopic.t", 0, 0, "message1")).get();
		producerSenderConfig.sender()
				.send(new ProducerRecord<Integer, String>("springKafkaDemoTopic.t", 0, 0, "message2")).get();

		ConsumerReceiver consumerReceiver = consumerConfig.receiver();
		Collection<String> topics = new ArrayList<String>(0);
		topics.add("springKafkaDemoTopic.t");
		consumerReceiver.subscribe(topics);

		try {
			consumerReceiver.onReceiveMessage();

		} catch (Exception e) {
			e.printStackTrace();
			producerSenderConfig.sender().close();
			consumerReceiver.close();
		} finally {
			producerSenderConfig.sender().close();
			consumerReceiver.close();
		}

	}

}
