package it.unisannio.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	@GetMapping(value="/sample", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Optional<Sample>> getSampleById(@RequestParam String id) {
		return ResponseEntity.ok(service.getSampleByTagId(id));
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

	// query arco temporale
	@GetMapping(value="/between", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Sample>> getAllSamplesByDate() throws ParseException {//TODO
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date from = sdf.parse("2021-07-17");
		Date to = sdf.parse("2021-08-17");
		return ResponseEntity.ok(service.getSamplesBetweenDate(from, to));
	}


	// query disposal conferiti
	@GetMapping(value="/disposal")
	public String getDisposalConferiti(@RequestParam String id) {
		
		if(service.existsById(id)) return "Disposal con TagID " + id + " conferito ";
		else return "Disposal con TagID " + id + " non conferito ";

	}



}
