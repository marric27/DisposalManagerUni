package it.unisannio.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalPosition;
import org.springframework.beans.factory.annotation.Autowired;

import it.unisannio.model.Sample;
import it.unisannio.model.SampleDao;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ServiceImpl implements SampleService {
	private final Logger LOG = LoggerFactory.getLogger(ServiceImpl.class);

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

	@Override
	public List<Sample> getSamplesByDistance(double lat, double lon, double dis) {
		return this.coordinateDistanceCompare(lat, lon, dis);
	}

	public List<Sample> coordinateDistanceCompare(double lat, double lon, double dis) {
		GeodeticCalculator geoCalc = new GeodeticCalculator();
		Ellipsoid reference = Ellipsoid.WGS84;
		GlobalPosition disposalPoint = new GlobalPosition(lat, lon, 0.0); // Point A

		List<Sample> samples = sampleDao.findAll();
		List<Sample> around = new ArrayList<>();
		for (Sample sample : samples) {
			double realLat = sample.getLatitude();
			double realLon = sample.getLongitude();

			GlobalPosition realPosition = new GlobalPosition(realLat, realLon, 0.0); // Point B
			double distance = geoCalc.calculateGeodeticCurve(reference, realPosition, disposalPoint)
					.getEllipsoidalDistance(); // Distance between Point A and Point B

			//LOG.info("Distanza in metri tra i due punti : {}", distance);
			if (distance <= dis) around.add(sample);
		}		
		return around;
	}

}
