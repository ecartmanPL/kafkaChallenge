package net.siekiera.kafkachallenge.utils;

import net.siekiera.kafkachallenge.SimpleKafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by W. Siekiera on 14.01.2018
 */
public class KafkaChallengeUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaChallengeUtils.class);
    public static void waitForPartitionsToBeAssigned(PartitionsAssignmentVerificator consumer) throws InterruptedException {
        while (!consumer.isPartitionsAssigned()) {
            Thread.sleep(500);
            LOGGER.info("Waiting to assign partitions");
        }
    }
}
