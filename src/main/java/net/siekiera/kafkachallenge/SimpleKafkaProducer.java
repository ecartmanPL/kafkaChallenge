package net.siekiera.kafkachallenge;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * Created by W. Siekiera on 11.01.2018
 */
public class SimpleKafkaProducer {
    private Producer<String, String> producer;

    public void setProducer(Producer<String, String> producer) {
        this.producer = producer;
    }

    public void produce(String topic, String key, String value) {
        producer.send(new ProducerRecord<String, String>(topic, key, value));
    }
}
