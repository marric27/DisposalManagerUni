package it.unisannio.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping(value = "/api_v1")
public class KafkaController {

	@Autowired
	KafkaProducerService kafkaProducer;

	@Autowired
	SampleService service;

	@GetMapping(value = "/sample", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Optional<Sample>> getSampleById(@RequestParam String id) {
		return ResponseEntity.ok(service.getSampleByTagId(id));
	}

	@GetMapping(value = "/samples", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Sample>> getAllSamples() {
		return ResponseEntity.ok(service.getAllSamples());
	}

	@PostMapping("/producer")
	public String sendMessage(@RequestBody JsonNode sample) {
		kafkaProducer.send(sample);
		return "Message sent successfully to the Kafka topic topic  " + sample;
	}

	// query arco temporale
	@GetMapping(value = "/between", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Sample>> getAllSamplesByDate(
			@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
			@RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to) throws ParseException {

		return ResponseEntity.ok(service.getSamplesBetweenDate(from, to));
	}

	// query disposal conferiti
	@GetMapping(value = "/disposal")
	public ResponseEntity<String> getConferredDisposal(@RequestParam List<String> id) {
		String output = "";
		for (String s : id) {
			if (service.existsById(s))
				output += "Il Disposal con TagID <b>" + s + "</b> è stato <b>conferito</b> <br>";
			else
				output += "Il Disposal con TagID <b>" + s + "</b> è <b>non conferito</b> <br>";
		}
		return ResponseEntity.ok(output);
	}

	// query disposal per distanza
	@GetMapping(value = "/distance", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Sample>> getSamplesByDistance(
			@RequestParam("lat") double lat,
			@RequestParam("lon") double lon,
			@RequestParam("dis") double dis) {

		return ResponseEntity.ok(service.getSamplesByDistance(lat, lon, dis));
	}


}
