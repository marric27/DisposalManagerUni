package it.unisannio.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleDao extends MongoRepository<Sample, String> {

	

}
