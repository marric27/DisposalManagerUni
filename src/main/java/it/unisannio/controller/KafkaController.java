package it.unisannio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import it.unisannio.model.Sample;
import it.unisannio.service.KafkaProducerService;
import it.unisannio.service.SampleService;

@RestController
@RequestMapping(value = "/controller")
public class KafkaController {

	@Autowired
	KafkaProducerService kafkaProducer;
	@Autowired
	SampleService service;

	@GetMapping("/producer")
	public String hello() {
		System.out.println(service.getAllSamples());
		return "Hello";
	}

	@PostMapping("/producer")
	public String sendMessage(@RequestBody JsonNode sample)
	{
		kafkaProducer.send(sample);
		return "Message sent successfully to the Kafka topic topic  "+sample;
	}

}
