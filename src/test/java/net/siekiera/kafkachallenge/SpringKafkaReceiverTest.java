package net.siekiera.kafkachallenge;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.sun.org.apache.regexp.internal.RE;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class SpringKafkaReceiverTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringKafkaReceiverTest.class);
    private static String RECEIVER_TOPIC = "receiver.t";
    private KafkaTemplate<String, String> template;

    private Producer<String, String> kafkaProducer;

//  @Autowired
//  private Receiver receiver;

    @Autowired
    private SimpleKafkaConsumer simpleKafkaConsumer;


    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, 1, RECEIVER_TOPIC);

    @Before
    public void setUp() throws Exception {
        // set up the Kafka producer properties
        Map<String, Object> senderProperties =
                KafkaTestUtils.senderProps(embeddedKafka.getBrokersAsString());

        // create a Kafka producer factory
        ProducerFactory<String, String> producerFactory =
                new DefaultKafkaProducerFactory<String, String>(senderProperties);

        kafkaProducer = producerFactory.createProducer();

        // create a Kafka template
        template = new KafkaTemplate<>(producerFactory);
        // set the default topic to send to
        template.setDefaultTopic(RECEIVER_TOPIC);

        // wait until the partitions are assigned
        for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry
                .getListenerContainers()) {
            ContainerTestUtils.waitForAssignment(messageListenerContainer,
                    embeddedKafka.getPartitionsPerTopic());
        }
    }

    @Test
    public void testReceive() throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        String message = "Send unique message " + UUID.randomUUID().toString();

        executorService.submit(simpleKafkaConsumer);

//        for (int i = 0; i < 1; i++) {
//
//            System.out.println("Producing message #" + i + " " + record.toString());
//        }
        kafkaProducer.send(new ProducerRecord<>(RECEIVER_TOPIC, message));

        //then
        executorService.awaitTermination(5, TimeUnit.SECONDS);
        executorService.shutdown();
        assertThat(simpleKafkaConsumer.getConsumerMessages().get(0).value()).isEqualTo(message);

    }
}
