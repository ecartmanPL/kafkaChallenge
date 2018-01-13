package net.siekiera.kafkachallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication

public class KafkachallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkachallengeApplication.class, args);
	}
}
