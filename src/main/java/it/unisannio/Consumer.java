package it.unisannio;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

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
			if(s.getOccurency()>25000) { // TODO valore soglia da definire 
				if(s.getTimestamp().getHour()<20 && s.getTimestamp().getHour()>17) { // soglia da scegliere
					System.out.println("Received User information : " + jsonNode);
					// carica in db
					service.saveSample(s);
				}
			}
			//} else System.out.println("questo elemento esiste gia in db : " + jsonNode);
		}
	}


}

/* TODO 
 * 2- finire di implementare le ricerche di anomalie (posizione - duplicati)
 * 4- usare un solo db e filtrare appena arrivano i dati
 * 5!!! - capire il flusso di dati come avviene (L'id è univoco?) l'id non è univoco = esistono piu disposal nel log con id uguale -> vanno filtrati
 */


