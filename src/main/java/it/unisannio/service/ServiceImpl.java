package it.unisannio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import it.unisannio.model.Sample;
import it.unisannio.model.SampleDao;
import org.springframework.stereotype.Service;

@Service
public class ServiceImpl implements SampleService {
	
	@Autowired
	SampleDao sampleDao;

	@Override
	public Optional<Sample> getSampleById(String id) {
		
		return sampleDao.findById(id);
	}

	@Override
	public Sample saveSample(Sample sample) {
		return sampleDao.save(sample);
	}

	@Override
	public boolean existsById(String id) {
		return sampleDao.existsById(id);
	}

	@Override
	public List<Sample> getAllSamples() {
		return sampleDao.findAll();
	}

}
