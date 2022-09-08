package it.unisannio;

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
	/*
	 * opto per una soluzione con un solo db per evitare complicazioni dovute al flusso dati continuo 
	 * 
	// Creating a Mongo client
	static MongoClient mongo = new MongoClient("localhost", 27017);
	// Accessing the database
	MongoDatabase database = mongo.getDatabase("dbRaw2");
	static DB db = mongo.getDB("dbRaw2");
	static DBCollection collection = db.getCollection("samples");
	 */
	
	private final Logger LOG = LoggerFactory.getLogger(Consumer.class);
	
	@Autowired
	SampleService service;

	ObjectMapper mapper = new ObjectMapper();

	@KafkaListener(topics = "topic", groupId = "group")
	public void listen(JsonNode jsonNode) throws JsonMappingException, JsonProcessingException, ParseException, ClassNotFoundException, SQLException {
		Sample s = mapper.readValue(jsonNode.toString(), Sample.class);

		//if(service.existsById(s.getTagID())) System.out.println("gia esiste" + jsonNode);

		if(s.getTagID().startsWith("57434F4D50")) {
			// verifica se l'id è già presente con query al db e decidere come gestire la situazione 
			//if(!service.existsById(s.getTagID())) {
			if(s.getOccurency()>2000) { // TODO valore soglia da definire 
				if(s.getTimestamp().getHour()<20 && s.getTimestamp().getHour()>1) { // TODO soglia da scegliere
					if(coordinateDistanceCompare(s)) {
						LOG.info("Received User information : {}", jsonNode);
						// carica in db
						service.saveSample(s);
					}
				}
			}
			//} else System.out.println("questo elemento esiste gia in db : " + jsonNode);
		}
	}

	

	// coordinate
	public boolean coordinateDistanceCompare(Sample s) throws JsonMappingException, JsonProcessingException {
		GeodeticCalculator geoCalc = new GeodeticCalculator(); 
		Ellipsoid reference = Ellipsoid.WGS84; 
		GlobalPosition disposalPoint = new GlobalPosition(s.getLatitude(), s.getLongitude(), 0.0); // Point A 

		// call service
		double realLat=0, realLon=0; // collectionpoint coord
		RestTemplate restTemplate = new RestTemplate();

		String result = 
				restTemplate.getForObject(
						"http://localhost:8081/"+s.getTagID(),
						String.class
						);
		LOG.info("Coordinate reali restituite dall'altro servizio : {}", result);
		
		ObjectMapper mapper = new ObjectMapper();
		CollectionPoint cp = mapper.readValue(result, CollectionPoint.class);
		
		realLat = cp.getLat();
		realLon = cp.getLon();

		GlobalPosition realPosition = new GlobalPosition(realLat, realLon, 0.0); // Point B 
		double distance = geoCalc.calculateGeodeticCurve(reference, realPosition, disposalPoint).getEllipsoidalDistance(); // Distance between Point A and Point B 

		LOG.info("Distanza in metri tra i due punti : {}", distance);
		return distance < 50 ? true : false; // TODO soglia da definire
	}
	
	public static class CollectionPoint {
		private double lat, lon;
		public CollectionPoint() {};
		public CollectionPoint(double lat, double lon) {
			super();
			this.lat = lat;
			this.lon = lon;
		}
		public double getLat() { return lat; }
		public void setLat(double lat) { this.lat = lat; }
		public double getLon() { return lon; }
		public void setLon(double lon) { this.lon = lon; }
	}
}

/* TODO 
 * 2- finire di implementare le ricerche di anomalie (duplicati)
 * 4- usare un solo db e filtrare appena arrivano i dati
 * 5!!! - capire il flusso di dati come avviene (L'id è univoco?) l'id non è univoco = esistono piu disposal nel log con id uguale -> vanno filtrati
 */


