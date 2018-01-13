package net.siekiera.kafkachallenge;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * Created by W. Siekiera on 13.01.2018
 */
public class SimpleKafkaConsumer implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleKafkaConsumer.class);
    private KafkaConsumer<String, String> kafkaConsumer;
    private List<ConsumerRecord<String, String>> consumerMessages = new LinkedList<>();

    public SimpleKafkaConsumer(Map<String, Object> properties, String topic) {
        kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Collections.singletonList(topic));
    }

    public SimpleKafkaConsumer() {
    }

    public void subscribe(String topic) {
        kafkaConsumer.subscribe(Collections.singletonList(topic));
    }

    public void setKafkaConsumer(KafkaConsumer<String, String> kafkaConsumer) {
        this.kafkaConsumer = kafkaConsumer;
    }

    public List<ConsumerRecord<String, String>> getConsumerMessages() {
        return consumerMessages;
    }

    @Override
    public void run() {
        try {
            while (true) {
                LOGGER.info("Trying to consume");
                ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    LOGGER.debug("topic = {}, partition = {}, offset = {}, key = {}, value = {}",
                            record.topic(),
                            record.partition(),
                            record.offset(),
                            record.key(),
                            record.value());
                    consumerMessages.add(record);
                }
            }
        } finally {
            kafkaConsumer.close();
        }
    }
}
