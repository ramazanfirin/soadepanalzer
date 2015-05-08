package com.avea.dependencyanalyzer.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.avea.dependencyanalyzer.model.BusinessNode;
import com.avea.dependencyanalyzer.model.ProcessNode;
import com.avea.dependencyanalyzer.model.ProjectNode;
import com.avea.dependencyanalyzer.model.ProxyNode;
import com.avea.dependencyanalyzer.model.QueneNode;
import com.avea.dependencyanalyzer.model.ServiceNode;



public interface ServiceNodeRepository extends GraphRepository<ServiceNode> {
	
	
	
	@Query("start n=node(*) where n.name={0} return n")
    public List<ServiceNode> searchByName(String serviceName);
	
	@Query("start n=node:name(name={0}) return n")
    public List<ServiceNode> searchByNameByIndex(String serviceName);
	                                         
	@Query("start n=node:__types__(className=\"com.avea.dependencyanalyzer.model.ProjectNode\") where n.name = {0} return n")
    public ProjectNode getProject(String name);
	
	@Query("start n=node:__types__(className=\"com.avea.dependencyanalyzer.model.ProjectNode\") where n.name = {0} return n.name")
    public String getProjectName(String name);
	
	@Query("start n=node:__types__(className=\"com.avea.dependencyanalyzer.model.BusinessNode\") where n.name = {0} return n")
    public BusinessNode getBusinessService(String name);
	
	@Query("start n=node:__types__(className=\"com.avea.dependencyanalyzer.model.ProxyNode\") where n.name = {0} return n")
    public ProxyNode getProxyService(String name);
	
	@Query("start n=node:__types__(className=\"com.avea.dependencyanalyzer.model.ProcessNode\") where n.name = {0} return n")
    public ProcessNode getProcessService(String name);
	
	
	@Query("start n=node:__types__(className=\"com.avea.dependencyanalyzer.model.ServiceNode\") where has(n.name) and n.name = {0} return n")
    public ServiceNode getServiceNode(String name);
	
	@Query("start n=node:__types__(className=\"com.avea.dependencyanalyzer.model.QueneNode\") where has(n.name) and n.name = {0} return n")
    public QueneNode getQueneNode(String name);
	
	@Query("START n = node(*) DELETE n")
    public void deleteAllNodes();
	
	@Query("START n = rel(*)  DELETE n")
    public void deleteAllRelations();
}
