package it.unisannio;

import java.sql.SQLException;
import java.text.ParseException;

import org.bson.Document;
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
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.util.JSON;

import it.unisannio.model.Sample;
import it.unisannio.service.SampleService; 

@Component
public class Consumer {

	//	// Creating a Mongo client
	//	static MongoClient mongo = new MongoClient("localhost", 27017);
	//	// Accessing the database
	//	MongoDatabase database = mongo.getDatabase("Disposal");
	//	static DB db = mongo.getDB("Disposal");
	//	static DBCollection collection = db.getCollection("samples");

	MongoClient mongoClient = new MongoClient();
	MongoDatabase db = mongoClient.getDatabase("Disposal");
	MongoCollection<Document> collection = db.getCollection("samples");



	private final Logger LOG = LoggerFactory.getLogger(Consumer.class);

	@Autowired
	SampleService service;

	ObjectMapper mapper = new ObjectMapper();

	@KafkaListener(topics = "topic", groupId = "group")
	public void listen(JsonNode jsonNode) throws JsonMappingException, JsonProcessingException, ParseException, ClassNotFoundException, SQLException {
		Sample s = mapper.readValue(jsonNode.toString(), Sample.class);
		LOG.info("Received Sample information : {}", jsonNode);



		if(s.getTagID().startsWith("57434F4D50")) {
			// verifica se l'id è già presente con query al db e decidere come gestire la situazione 
			//if(collection.find(Filters.exists("tagID")).first()==null) {
			if(s.getOccurency()>2000) { // TODO valore soglia da definire 
				if(s.getTimestamp().getHour()<20 && s.getTimestamp().getHour()>1) { // TODO soglia da scegliere
					if(coordinateDistanceCompare(s)) {
						if(collection.find(Filters.exists("tagID")).first()==null) {
							Document doc = new Document();
							doc.put("tagID", s.getTagID());
							doc.put("timestamp", s.getTimestamp());
							doc.put("truckID", s.getTruckID());
							doc.put("occurency", s.getOccurency());
							doc.put("latitude", s.getLatitude());
							doc.put("longitude", s.getLongitude());
							collection.insertOne(doc);
							LOG.info("Sample inserted: {}", jsonNode);
						} else {
							Document check = collection.find(Filters.exists("tagID")).first();
							System.out.println(check);
							int occ = check.getInteger("occurency");
							
							if(s.getOccurency()>occ) {
								Document doc = new Document();
								doc.put("tagID", s.getTagID());
								doc.put("timestamp", s.getTimestamp());
								doc.put("truckID", s.getTruckID());
								doc.put("occurency", s.getOccurency());
								doc.put("latitude", s.getLatitude());
								doc.put("longitude", s.getLongitude());
								collection.insertOne(doc);
								
								collection.deleteOne(Filters.eq("tagID", check.get("tagID")));
								LOG.info("Sample inserted: {}", jsonNode);
							}
						}
					}
				}
			}
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
		return distance < 5000 ? true : false; // TODO soglia da definire
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
