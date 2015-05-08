package com.avea.dependencyanalyzer.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.rest.graphdb.entity.RestNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;

import com.avea.dependencyanalyzer.model.BusinessNode;
import com.avea.dependencyanalyzer.model.CallRelationEntity;
import com.avea.dependencyanalyzer.model.ProcessNode;
import com.avea.dependencyanalyzer.model.ProjectNode;
import com.avea.dependencyanalyzer.model.ProxyNode;
import com.avea.dependencyanalyzer.model.QueneNode;
import com.avea.dependencyanalyzer.model.ServiceNode;
import com.avea.dependencyanalyzer.repository.CallRelationRepository;
import com.avea.dependencyanalyzer.repository.ServiceNodeRepository;
import com.avea.dependencyanalyzer.util.ConvertUtil;





@Service
public class ServiceNodeService {
	
	@Autowired(required=true)
	private ServiceNodeRepository serviceNodeRepository;
	
	@Autowired(required=true)
	private CallRelationRepository callRelationRepository;
	
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	ServiceNode referanceNode ;
	
	@Autowired(required=true)
	Neo4jTemplate neo4jTemplate;
	
	public ServiceNodeService() {
		super();
				
	}
	
	public ServiceNode getServiceCodeByNativeQuery(String name){
		RestNode restNode = searchByNativeQuery(name);
		ServiceNode node = convertToServiceNode(restNode);
		return node;
	}
	
	public ServiceNode convertToServiceNode(RestNode node){
		ServiceNode root= ConvertUtil.convertServiceNode(node);
		
		Iterator<Relationship> it =  node.getRelationships(Direction.OUTGOING).iterator();
		while (it.hasNext()) {
			Relationship relationship = (Relationship) it.next();
			Node endNode = neo4jTemplate.getNode(relationship.getEndNode().getId());
			ServiceNode endNode2 = ConvertUtil.convertServiceNode((RestNode)endNode);
			String methodName ="";
			
			if(relationship.getType().name().equals("calls")){
				if(relationship.hasProperty("methodName"))
					methodName = (String)relationship.getProperty("methodName");
				CallRelationEntity callRelationEntity = new CallRelationEntity(root,endNode2,methodName);
				//root.getServiceList().add(callRelationEntity);
			}
			
			if(relationship.getType().name().equals("contains")){
				//root.getContainsList().add(endNode2);
			}
		}
		
		
		 it =  node.getRelationships(Direction.INCOMING).iterator();
		while (it.hasNext()) {
			Relationship relationship = (Relationship) it.next();
			Node startNode = neo4jTemplate.getNode(relationship.getStartNode().getId());
			ServiceNode startNode2 = ConvertUtil.convertServiceNode((RestNode)startNode);
			
			
			String methodName ="";
			
			if(relationship.getType().name().equals("calls")){
				if(relationship.hasProperty("methodName"))
					methodName = (String)relationship.getProperty("methodName");
				CallRelationEntity callRelationEntity = new CallRelationEntity(startNode2,root,methodName);
				//root.getInComingServiceList().add(callRelationEntity);
			}
			
			if(relationship.getType().name().equals("contains")){
				root.setProject((ProjectNode)startNode2);
			}
		}
		
		return root;
	}
	
	public RestNode searchByNativeQuery(String name){
		Result<Map<String,Object>> results  = neo4jTemplate.query("START root=node(*)  WHERE has(root.name) and root.name ='"+name+"' RETURN root ", null);
		for (Iterator iterator = results.iterator(); iterator.hasNext();) {
			HashMap<String , Object> map = ( HashMap<String , Object>)iterator.next();
			for (Iterator iterator2 = map.keySet().iterator(); iterator2.hasNext();) {
				String type = (String) iterator2.next();
				RestNode node = (RestNode)map.get(type);node.getRelationships();
				return node;
			}
		}
		return null;
	}
	
	public List<ServiceNode> search(String parameter){
		neo4jTemplate.getGraphDatabase();
		List<ServiceNode> resultList = new ArrayList<ServiceNode>();
		
		Result<Map<String,Object>> results  = neo4jTemplate.query("START root=node(*)  WHERE root.name =~ '(?i).*"+parameter+".*' RETURN root  ", null);
		
		
		for (Iterator iterator = results.iterator(); iterator.hasNext();) {
			HashMap<String , Object> map = ( HashMap<String , Object>)iterator.next();
			for (Iterator iterator2 = map.keySet().iterator(); iterator2.hasNext();) {
				String type = (String) iterator2.next();
				RestNode node = (RestNode)map.get(type);
				ServiceNode serviceNode= findWorldById(node.getId());
				
				resultList.add(serviceNode);
				
				
			}
		
		} 
		
		return resultList;
		
	}
	
	public List<String> searchName(String parameter){
		neo4jTemplate.getGraphDatabase();
		List<String> resultList = new ArrayList<String>();
		
		Result<Map<String,Object>> results  = neo4jTemplate.query("START root=node(*)  WHERE has(root.name) and root.name =~ '(?i).*"+parameter+".*' RETURN root  ", null);
		
		
		for (Iterator iterator = results.iterator(); iterator.hasNext();) {
			HashMap<String , Object> map = ( HashMap<String , Object>)iterator.next();
			for (Iterator iterator2 = map.keySet().iterator(); iterator2.hasNext();) {
				String type = (String) iterator2.next();
				RestNode node = (RestNode)map.get(type);
				//ServiceNode serviceNode= findWorldById(node.getId());
				
				resultList.add((String)node.getProperty("name"));
				
				
			}
		
		} 
		
		return resultList;
		
	}
	
	public void save(CallRelationEntity object) {
		callRelationRepository.save(object);
	}	
	
	public String getProjectName(String name) {
		return serviceNodeRepository.getProjectName(name);
	}
	
	public void save(ServiceNode object) {
		serviceNodeRepository.save(object);
	}
	
	public long getCount() {
		return serviceNodeRepository.count();
	}
	
	public Iterable<ServiceNode> getAll() {
		return serviceNodeRepository.findAll();
	}
	
	public ServiceNode findWorldById(Long id) {
		return (ServiceNode)serviceNodeRepository.findOne(id);
	}
	
	public ServiceNode findByName(String name) {
		return serviceNodeRepository.findByPropertyValue("name", name);
	}
	
	public Iterable<ServiceNode> findAllByNumberOfMoons(int numberOfMoons) {
		return serviceNodeRepository.findAllByPropertyValue("moons", numberOfMoons);
	}

	public CallRelationEntity findRelation(Long id) {
		return callRelationRepository.findOne(id);
	}
	
	public List<ServiceNode> searchByName(String name) {
		return serviceNodeRepository.searchByName(name);
	}
	
	public List<ServiceNode> searchByNameByIndex(String name) {
		return serviceNodeRepository.searchByNameByIndex(name);
	}
	
	public ServiceNode getServiceNode(String name){
		 return serviceNodeRepository.getServiceNode(name);
	 }
	
	 public ProjectNode getProject(String name){
		 return serviceNodeRepository.getProject(name);
	 }
	 
	 public BusinessNode getBusinessNode(String name){
		 return serviceNodeRepository.getBusinessService(name);
	 }
	 
	 public ProxyNode getProxyNode(String name){
		 return serviceNodeRepository.getProxyService(name);
	 }

	 public ProcessNode getProcessNode(String name){
		 return serviceNodeRepository.getProcessService(name);
	 }

	 public QueneNode getQueneNode(String name){
		 return serviceNodeRepository.getQueneNode(name);
	 }

	 public void deleteAll(){
		  serviceNodeRepository.deleteAllRelations();
		 serviceNodeRepository.deleteAllNodes();

	 }
	 
	 public List<CallRelationEntity> getAllRelations(){
		return   callRelationRepository.getAllRelatins();
	 }
	 
	 
	 
	 
	 

	
	
	
	
}
