package it.unisannio.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Component
public class Scheduler {

	private static final Logger log = LoggerFactory.getLogger(Scheduler.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	//@Scheduled(cron = "*/5 * * * * *")
	public void currentTime() {
		log.info("Current Time      = {}", dateFormat.format(new Date()));
	}

	//@Scheduled(cron = "*/50 * * * * *")
	public void filter() {
		// Creating a Mongo client
		MongoClient mongo = new MongoClient("localhost", 27017);
		// Accessing the database
		MongoDatabase database = mongo.getDatabase("dbRaw2");
		MongoCollection<Document> collection = database.getCollection("samples");

		BasicDBObject criteria = new BasicDBObject("Timestamp", new BasicDBObject("$regex", "[0-9]{4}-[0-1][1-9]-[0-3][0-9] 0[5-9]")// TODO fascia oraria da definire
				.append("$options", "i")
				);

		// soglia di occorrenze che il sample deve superare per essere considerato valido 
		Bson soglia = new Document("$gt", 10000); // TODO soglia da definire
		criteria.append("Occurency", soglia);

		FindIterable<Document> cur = collection.find(criteria);
		Iterator it = cur.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}



}