package it.unisannio.model;

import java.util.Optional;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleDao extends MongoRepository<Sample, String> {

    @Query("{tagID : ?0}")
    Optional<Sample> findByTagID(String id);

	@DeleteQuery
	void deleteByTagID(String tagid);

}
