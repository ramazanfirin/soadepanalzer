package com.avea.dependencyanalyzer.model;

import org.springframework.data.neo4j.annotation.NodeEntity;


@NodeEntity
public class ProxyNode extends ServiceNode{

	@Override
	public String getType() {
		return "Proxy";
	}


	
    
}
