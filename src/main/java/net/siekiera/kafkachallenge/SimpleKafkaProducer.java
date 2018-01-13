package net.siekiera.kafkachallenge;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * Created by W. Siekiera on 11.01.2018
 */
public class SimpleKafkaProducer {
    private KafkaProducer<String, String> producer;

    public void setProducer(KafkaProducer<String, String> producer) {
        this.producer = producer;
    }

    public void produce(String topic, String key, String value) {
        producer.send(new ProducerRecord<String, String>(topic, key, value));
    }
}
