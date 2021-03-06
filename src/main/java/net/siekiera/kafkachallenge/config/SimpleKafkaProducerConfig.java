package net.siekiera.kafkachallenge.config;

import net.siekiera.kafkachallenge.SimpleKafkaProducer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Created by W. Siekiera on 11.01.2018
 */
@Configuration
public class SimpleKafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    String bootstrapServer;

    @Bean
    public SimpleKafkaProducer createSimpleKafkaProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServer);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        SimpleKafkaProducer simpleKafkaProducer = new SimpleKafkaProducer();
        simpleKafkaProducer.setProducer(new KafkaProducer<>(props));
        return simpleKafkaProducer;
    }
}
