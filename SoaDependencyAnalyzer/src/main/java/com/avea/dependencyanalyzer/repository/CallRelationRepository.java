package com.avea.dependencyanalyzer.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.avea.dependencyanalyzer.model.CallRelationEntity;
import com.avea.dependencyanalyzer.model.ServiceNode;

public interface CallRelationRepository extends GraphRepository<CallRelationEntity>{
	
	@Query("START root=rel(*) RETURN root ")
    public List<CallRelationEntity> getAllRelatins();
}
