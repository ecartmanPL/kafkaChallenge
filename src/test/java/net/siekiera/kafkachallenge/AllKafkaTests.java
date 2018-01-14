package net.siekiera.kafkachallenge;

import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.kafka.test.rule.KafkaEmbedded;

/**
 * Created by W. Siekiera on 14.01.2018
 */
@RunWith(Suite.class)
@SuiteClasses({SpringKafkaSenderTest.class,
        SpringKafkaReceiverTest.class})
public class AllKafkaTests {
    public static String RECEIVER_TOPIC = "receiver.t";
    public static String SENDER_TOPIC = "sender.t";

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, 1, RECEIVER_TOPIC, SENDER_TOPIC);

}
