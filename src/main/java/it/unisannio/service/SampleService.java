package it.unisannio.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import it.unisannio.model.Sample;

public interface SampleService {
	
	Optional<Sample> getSampleByTagId(String id);
	
	Sample saveSample(Sample sample);
	
	boolean existsById(String id);
	
	List<Sample> getAllSamples();

	void deleteSampleByTagId(String tagid);

	List<Sample> getSamplesBetweenDate(Date from, Date to);

	//List<Sample> getSamplesByDistance
}
