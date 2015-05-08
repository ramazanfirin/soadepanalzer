package com.avea.dependencyanalyzer.model;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class ProjectNode extends ServiceNode{

	@Override
	public String getType() {
		return "Project";
	}

	

	
}
