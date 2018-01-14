package net.siekiera.kafkachallenge.config;

import net.siekiera.kafkachallenge.SimpleKafkaConsumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Created by W. Siekiera on 13.01.2018
 */
@Configuration
public class SimpleKafkaConsumerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${kafka.topic.receiver}")
    private String topic;

    @Bean
    public SimpleKafkaConsumer simpleKafkaConsumer() {
        SimpleKafkaConsumer simpleKafkaConsumer = new SimpleKafkaConsumer();
        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("group.id", "group1");
        properties.put("enable.auto.commit", true);
        properties.put("key.deserializer", StringDeserializer.class.getName());
        properties.put("value.deserializer", StringDeserializer.class.getName());
        simpleKafkaConsumer.setKafkaConsumer(new KafkaConsumer<>(properties));
        simpleKafkaConsumer.subscribe(topic);
        return simpleKafkaConsumer;
    }
}
