package com.avea.dependencyanalyzer.util;

import java.util.Iterator;

import org.eclipse.jdt.internal.compiler.ast.WhileStatement;
import org.neo4j.rest.graphdb.entity.RestNode;

import com.avea.dependencyanalyzer.model.BusinessNode;
import com.avea.dependencyanalyzer.model.ProcessNode;
import com.avea.dependencyanalyzer.model.ProjectNode;
import com.avea.dependencyanalyzer.model.ProxyNode;
import com.avea.dependencyanalyzer.model.QueneNode;
import com.avea.dependencyanalyzer.model.ServiceNode;

public class ConvertUtil {

	
	public static ServiceNode convertServiceNode(RestNode restNode){
		
		ServiceNode node = null;
		String type =(String) restNode.getProperty("__type__");
		
		if(type.indexOf("Business")>0)
			node=new BusinessNode();
		if(type.indexOf("Proxy")>0)
			node= new ProxyNode(); 
		if(type.indexOf("Process")>0)
			node= new ProcessNode(); 
		if(type.indexOf("Project")>0)
			node= new ProjectNode(); 
		if(type.indexOf("Quene")>0)
			node= new QueneNode();
		
		
		node.setBindingRequest((String) restNode.getProperty("BindingRequest",""));
		node.setBindingResponse((String) restNode.getProperty("BindingResponse",""));
		node.setEndPointProtocol((String) restNode.getProperty("endPointProtocol",""));
		node.setEndPointUri((String) restNode.getProperty("endPointuri",""));
		node.setName((String) restNode.getProperty("name",""));
		
		Iterator<String> iterator=restNode.getPropertyKeys().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			String value = (String)restNode.getProperty(key);
			node.getProperties().setProperty(key, value);
		}
		
		
		return node;
	}
}
