package it.unisannio.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;


@Service
public class KafkaProducerService {
    private final Logger LOG = LoggerFactory.getLogger(KafkaProducerService.class);
    @Autowired
    private KafkaTemplate<String, JsonNode> kafkaTemplate;

    String kafkaTopic = "topic";

    public void send(JsonNode user) {
        LOG.info("Sending User Json Serializer : {}", user);
        kafkaTemplate.send(kafkaTopic, user);
    }
}
