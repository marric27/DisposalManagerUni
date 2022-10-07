package it.unisannio.service;

import java.util.Date;
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
	public Optional<Sample> getSampleByTagId(String id) {
		return sampleDao.findByTagID(id);
	}

	@Override
	public Sample saveSample(Sample sample) {
		return sampleDao.save(sample);
	}

	@Override
	public boolean existsById(String id) {
		return sampleDao.existsByTagID(id);
	}

	@Override
	public List<Sample> getAllSamples() {
		return sampleDao.findAll();
	}

	@Override
	public void deleteSampleByTagId(String tagid) {
		sampleDao.deleteByTagID(tagid);
	}

	@Override
	public List<Sample> getSamplesBetweenDate(Date from, Date to) {
		
		return sampleDao.findByTimestampBetween(from, to);
	}

}
