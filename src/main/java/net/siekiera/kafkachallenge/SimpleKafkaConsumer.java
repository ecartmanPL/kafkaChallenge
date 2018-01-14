package net.siekiera.kafkachallenge;

import net.siekiera.kafkachallenge.utils.PartitionsAssignmentVerificator;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by W. Siekiera on 13.01.2018
 */
public class SimpleKafkaConsumer extends PartitionsAssignmentVerificator implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleKafkaConsumer.class);
    private KafkaConsumer<String, String> kafkaConsumer;
    private List<ConsumerRecord<String, String>> consumerMessages = new LinkedList<>();

    public SimpleKafkaConsumer(Map<String, Object> properties, String topic) {
        kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Collections.singletonList(topic));
    }

    public SimpleKafkaConsumer() {
    }

    @Override
    public void run() {
        try {
            while (true) {
                LOGGER.info("Trying to consume");
                ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    LOGGER.info("topic = {}, partition = {}, offset = {}, key = {}, value = {}",
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

    public void subscribe(String topic) {
        kafkaConsumer.subscribe(Collections.singletonList(topic), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                partitionsAssigned = false;
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                partitionsAssigned = true;
            }
        });
    }

    public void setKafkaConsumer(KafkaConsumer<String, String> kafkaConsumer) {
        this.kafkaConsumer = kafkaConsumer;
    }

    public List<ConsumerRecord<String, String>> getConsumerMessages() {
        return consumerMessages;
    }

    public boolean isPartitionsAssigned() {
        return partitionsAssigned;
    }
}
