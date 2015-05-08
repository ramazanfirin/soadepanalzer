package com.avea.dependencyanalyzer.model;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;



@RelationshipEntity(type = "calls")  
public class CallRelationEntity{

	@GraphId 
	private Long id;
    
	@Fetch @StartNode 
    private ServiceNode startNode;
    
	@Fetch @EndNode 
    private ServiceNode endNode;
	
	private String methodName;
	

	public CallRelationEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CallRelationEntity(ServiceNode startNode, ServiceNode endNode,
			String methodName) {
		super();
		this.startNode = startNode;
		this.endNode = endNode;
		this.methodName = methodName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ServiceNode getStartNode() {
		return startNode;
	}

	public void setStartNode(ServiceNode startNode) {
		this.startNode = startNode;
	}

	public ServiceNode getEndNode() {
		return endNode;
	}

	public void setEndNode(ServiceNode endNode) {
		this.endNode = endNode;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
}
