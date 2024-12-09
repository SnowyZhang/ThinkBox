package com.snowy.thinkbox.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private static final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendVoteEvent(String topic, String message) {
        log.info("Sending message to Kafka topic: {}", topic);
        kafkaTemplate.send(topic, message);
    }
}
