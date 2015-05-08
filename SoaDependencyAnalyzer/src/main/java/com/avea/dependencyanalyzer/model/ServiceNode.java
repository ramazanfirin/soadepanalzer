package com.avea.dependencyanalyzer.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.annotation.RelatedToVia;
import org.springframework.data.neo4j.fieldaccess.DynamicProperties;
import org.springframework.data.neo4j.fieldaccess.DynamicPropertiesContainer;
import org.springframework.data.neo4j.support.index.IndexType;


@NodeEntity
public abstract class ServiceNode {
	
		private final static String REACHABLE_BY_ROCKET = "REACHABLE_BY_ROCKET";
	
    @GraphId
    private Long id;

    
    @Indexed(indexName="name",indexType=IndexType.UNIQUE)
    private String name;

    private String nodeType;
    
    private String bindingRequest;
    private String bindingResponse;
    
    private String endPointProtocol;
    private String endPointUri;
    
    
    private DynamicProperties properties = new DynamicPropertiesContainer();
    
    
    public abstract String getType();

    public List<Map.Entry<String, Object>>  getParameters(){
    	LinkedHashMap < String, Object> map = new LinkedHashMap <String, Object>();
    	map.put("name", name);
    	map.put("type", getType());
    	map.put("bindingRequest", bindingRequest);
    	map.put("bindingResponse", bindingResponse);
    	map.put("endPointProtocol", endPointProtocol);
    	map.put("endPointUri", endPointUri);
    	map.putAll(properties.asMap());
    	
    	 Set<Map.Entry<String, Object>> productSet = map.entrySet();
         return new ArrayList<Map.Entry<String, Object>>(productSet);
    	

    	
    }
    


	@Fetch
    @RelatedTo(type = "calls", direction = Direction.BOTH)
    private Set<ServiceNode> serviceNodeList = new HashSet<ServiceNode>();
	

//    @RelatedToVia(type="calls", direction = Direction.OUTGOING)
//    @Fetch    
//	private Set<CallRelationEntity> serviceList = new HashSet<CallRelationEntity>();
//    
//	@RelatedToVia(type="calls", direction = Direction.INCOMING)
//	@Fetch    
//	private Set<CallRelationEntity> inComingServiceList = new HashSet<CallRelationEntity>();

	@RelatedTo(type = "contains", direction = Direction.INCOMING)
	@Fetch    
    private ProjectNode project;
    

    @RelatedTo(type = "contains",direction= Direction.OUTGOING)
	@Fetch 
	private Set<ServiceNode> containsList = new HashSet<ServiceNode>();

    
    
    
    
    
//    public Set<CallRelationEntity> getInComingServiceList() {
//		return inComingServiceList;
//	}
//
//	public void setInComingServiceList(Set<CallRelationEntity> inComingServiceList) {
//		this.inComingServiceList = inComingServiceList;
//	}
//
//    
//    public Set<CallRelationEntity> getServiceList() {
//		return serviceList;
//	}
//
//	public void setServiceList(Set<CallRelationEntity> serviceList) {
//		this.serviceList = serviceList;
//	}
	
	public List<ServiceNode> getContainsAsList(){
		return new ArrayList<ServiceNode>(containsList);
	}
	
//	public List<CallRelationEntity> getIncomingServicesAsList(){
//		return new ArrayList<CallRelationEntity>(inComingServiceList);
//	}
//	
//	public List<CallRelationEntity> getOutgoingServicesAsList(){
//		return new ArrayList<CallRelationEntity>(serviceList);
//	}
	
	
	public Set<ServiceNode> getContainsList() {
		return containsList;
	}

	public void setContainsList(Set<ServiceNode> containsList) {
		this.containsList = containsList;
	}
	
   
    public static void main(String[] args) {
		System.out.println("ts");
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	


	public ServiceNode() {
		super();
		// TODO Auto-generated constructor stub
	}


	public ServiceNode(String name) {
		super();
		this.name = name;
	}


	


	public String getBindingRequest() {
		return bindingRequest;
	}


	public void setBindingRequest(String bindingRequest) {
		this.bindingRequest = bindingRequest;
	}


	public String getBindingResponse() {
		return bindingResponse;
	}


	public void setBindingResponse(String bindingResponse) {
		this.bindingResponse = bindingResponse;
	}


	public String getEndPointProtocol() {
		return endPointProtocol;
	}


	public void setEndPointProtocol(String endPointProtocol) {
		this.endPointProtocol = endPointProtocol;
	}


	public String getEndPointUri() {
		return endPointUri;
	}


	public void setEndPointUri(String endPointUri) {
		this.endPointUri = endPointUri;
	}


	public DynamicProperties getProperties() {
		return properties;
	}


	public void setProperties(DynamicProperties properties) {
		this.properties = properties;
	}


	public Set<ServiceNode> getServiceNodeList() {
		return serviceNodeList;
	}


	public void setServiceNodeList(Set<ServiceNode> serviceNodeList) {
		this.serviceNodeList = serviceNodeList;
	}


	public ProjectNode getProject() {
		return project;
	}


	public void setProject(ProjectNode project) {
		this.project = project;
	}


	public static String getReachableByRocket() {
		return REACHABLE_BY_ROCKET;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getNodeType() {
		return nodeType;
	}


	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
}
