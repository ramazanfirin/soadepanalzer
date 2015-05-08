package com.avea.dependencyanalyzer.model;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.RelatedTo;




public class ProcessNode extends ServiceNode{

	
	@Override
	public String getType() {
		return "Process";
	}


}
