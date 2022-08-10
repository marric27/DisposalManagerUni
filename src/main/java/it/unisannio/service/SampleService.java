package it.unisannio.service;

import java.util.Optional;

import it.unisannio.model.Sample;

public interface SampleService {
	
	Optional<Sample> getSampleById(String id);
	
	Sample saveSample(Sample sample);
	
	boolean existsById(String id);
}
