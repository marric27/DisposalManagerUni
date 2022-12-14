package it.unisannio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.sql.SQLException;
import java.text.ParseException;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.unisannio.model.Sample;
import it.unisannio.service.SampleService;

@Component
public class Consumer {
	private final Logger LOG = LoggerFactory.getLogger(Consumer.class);

	// costanti dei filtri
	final String PREFIX = "57434F4D50";
	final int OCCURRENCY = 2000;
	final int FROM_TIME = 1, TO_TIME = 20;
	final int DISTANCE = 2500;

	@Autowired
	SampleService service;

	ObjectMapper mapper = new ObjectMapper();

	@KafkaListener(topics = "topic5", groupId = "group")
	public void listen(JsonNode jsonNode)
			throws ParseException, ClassNotFoundException, SQLException, IOException {

		Sample s = mapper.readValue(jsonNode.toString(), Sample.class);
		ThreadManagerSingleton.getInstance().getStartFilter().process(s);














		// LOG.info("Received Sample information : {}", jsonNode);

		
/*
		if (s.getTagID().startsWith(PREFIX)) {
			if (s.getOccurency() > OCCURRENCY) {
				if (s.getTimestamp().getHour() < TO_TIME && s.getTimestamp().getHour() > FROM_TIME) {
					System.out.println("sample" +s);
					// if (coordinateDistanceCompare(s)) {
					if (!service.existsById(s.getTagID())) {
						LOG.info("sample non esistente quindi inserisco");
						service.saveSample(s);

						LOG.info("Sample inserted: {}", jsonNode);
					} else {
						LOG.info("sample esistente quindi confronto e inserisco");
						Sample oldsample = service.getSampleByTagId(s.getTagID()).orElseThrow();

						int occ = oldsample.getOccurency();

						if (s.getOccurency() >= occ) {
							service.deleteSampleByTagId(s.getTagID());
							service.saveSample(s);
							LOG.info("Sample updated: {}", jsonNode);
						}
					}
					// } else LOG.info("Sample not inserted due to wrong coordinate: {}", jsonNode);
				} else
					LOG.info("Sample not inserted due to wrong timestamp: {}", jsonNode);
			} else
				LOG.info("Sample not inserted due to wrong occurrency: {}", jsonNode);
		} else
			LOG.info("Sample not inserted due to wrong prefix: {}", jsonNode);
 */
	}

	// coordinate
	public boolean coordinateDistanceCompare(Sample s) throws JsonMappingException, JsonProcessingException {
		GeodeticCalculator geoCalc = new GeodeticCalculator();
		Ellipsoid reference = Ellipsoid.WGS84;
		GlobalPosition disposalPoint = new GlobalPosition(s.getLatitude(), s.getLongitude(), 0.0); // Point A

		// call service
		double realLat = 0, realLon = 0; // collectionpoint coord
		RestTemplate restTemplate = new RestTemplate();

		String result = null;
		try {
			result = restTemplate.getForObject(
					"http://localhost:8081/" + s.getTagID(),
					String.class);
			LOG.info("Coordinate reali restituite dall'altro servizio : {}", result);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info("Il servizio per la richiesta delle coordinate non risponde");
		}
		ObjectMapper mapper = new ObjectMapper();
		CollectionPoint cp = mapper.readValue(result, CollectionPoint.class);

		realLat = cp.getLat();
		realLon = cp.getLon();

		GlobalPosition realPosition = new GlobalPosition(realLat, realLon, 0.0); // Point B
		double distance = geoCalc.calculateGeodeticCurve(reference, realPosition, disposalPoint)
				.getEllipsoidalDistance(); // Distance between Point A and Point B

		LOG.info("Distanza in metri tra i due punti : {}", distance);
		return distance < DISTANCE ? true : false;
	}

	public static class CollectionPoint {
		private double lat, lon;

		public CollectionPoint() {
		};

		public CollectionPoint(double lat, double lon) {
			super();
			this.lat = lat;
			this.lon = lon;
		}

		public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

		public double getLon() {
			return lon;
		}

		public void setLon(double lon) {
			this.lon = lon;
		}
	}
}
