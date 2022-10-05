package it.unisannio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import it.unisannio.service.KafkaProducerService;
import it.unisannio.service.SampleService;

import it.unisannio.model.Sample;

@RestController
@CrossOrigin
@RequestMapping(value = "/controller")
public class KafkaController {

	@Autowired
	KafkaProducerService kafkaProducer;

	@Autowired
	SampleService service;

	@GetMapping("/producer")
	public String hello() {
		System.out.println(service.getSampleByTagId("57434F4D501A1C191F84EB7AEDDED770826AA5659E"));
		return "Hello";
	}

	@GetMapping(value="/samples", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Sample>> getAllSamples() {
		return ResponseEntity.ok(service.getAllSamples());
	}

	@PostMapping("/producer")
	public String sendMessage(@RequestBody JsonNode sample)	{
		kafkaProducer.send(sample);
		return "Message sent successfully to the Kafka topic topic  "+sample;
	}

}
