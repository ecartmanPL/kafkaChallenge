package net.siekiera.kafkachallenge;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

import net.siekiera.kafkachallenge.utils.KafkaChallengeUtils;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class SpringKafkaReceiverTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringKafkaReceiverTest.class);

    private KafkaTemplate<String, String> template;

    private Producer<String, String> kafkaProducer;

    @Autowired
    private SimpleKafkaConsumer simpleKafkaConsumer;

    @Before
    public void setup() {
        // set up the Kafka producer properties
        Map<String, Object> senderProperties =
                KafkaTestUtils.senderProps(AllKafkaTests.embeddedKafka.getBrokersAsString());

        // create a Kafka producer factory
        ProducerFactory<String, String> producerFactory =
                new DefaultKafkaProducerFactory<String, String>(senderProperties);

        kafkaProducer = producerFactory.createProducer();
    }

    @Test
    public void singleMessageReceiverTest() throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        String message = "Send unique message " + UUID.randomUUID().toString();

        //run consumer in single thread
        executorService.submit(simpleKafkaConsumer);
        KafkaChallengeUtils.waitForPartitionsToBeAssigned(simpleKafkaConsumer);

        //send 100 messages
        for (int i = 0; i < 100; i++) {
            kafkaProducer.send(new ProducerRecord<>(AllKafkaTests.RECEIVER_TOPIC, message));
        }
        executorService.awaitTermination(1, TimeUnit.SECONDS);
        executorService.shutdown();

        //consume and check
        assertThat(simpleKafkaConsumer.getConsumerMessages().get(0).value()).isEqualTo(message);
    }
}
