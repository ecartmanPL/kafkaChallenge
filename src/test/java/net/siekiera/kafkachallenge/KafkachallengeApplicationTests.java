package net.siekiera.kafkachallenge;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkachallengeApplicationTests {
	@Autowired
	SimpleKafkaProducer simpleKafkaProducer;

	@ClassRule
	public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, "topic");

	@Before
	public void setUp() throws Exception {


	}

	@Test
	public void contextLoads() {
		embeddedKafka.getBrokersAsString();
	}

}
