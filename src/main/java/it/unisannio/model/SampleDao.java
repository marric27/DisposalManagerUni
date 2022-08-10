package it.unisannio.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface SampleDao extends JpaRepository<Sample, String> {

	@Query(value = "SELECT * FROM tutorials", nativeQuery = true)
	List<Sample> findAll();

}
