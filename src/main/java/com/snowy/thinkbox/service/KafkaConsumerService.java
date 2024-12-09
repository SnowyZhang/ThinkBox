package com.snowy.thinkbox.service;

import jdk.jfr.Enabled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@EnableKafka
public class KafkaConsumerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

    private final WebSocketService webSocketService;

    @Autowired
    private KafkaConsumerService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @KafkaListener(topics = "vote-topic", groupId = "vote-group")
    public void consumeVoteEvent(String message) {
        webSocketService.sendMessage(message);
        log.info("Consumed message from Kafka: {}", message);
    }
}
