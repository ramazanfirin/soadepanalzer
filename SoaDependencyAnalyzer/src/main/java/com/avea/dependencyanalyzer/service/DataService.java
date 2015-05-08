package com.avea.dependencyanalyzer.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import avea.bpm.arch.model.Process;

import com.avea.dependencyanalyzer.model.BusinessNode;
import com.avea.dependencyanalyzer.model.CallRelationEntity;
import com.avea.dependencyanalyzer.model.ProcessNode;
import com.avea.dependencyanalyzer.model.ProjectNode;
import com.avea.dependencyanalyzer.model.ProxyNode;
import com.avea.dependencyanalyzer.model.QueneNode;
import com.avea.dependencyanalyzer.model.ServiceNode;
import com.avea.dependencyanalyzer.util.ParseUtil;
import com.tomecode.soa.beawli.services.WliBaseProcess;
import com.tomecode.soa.beawli.services.WliBaseService;
import com.tomecode.soa.ora.osb10g.services.BusinessService;
import com.tomecode.soa.ora.osb10g.services.OSBService;
import com.tomecode.soa.ora.osb10g.services.Proxy;
import com.tomecode.soa.ora.osb10g.services.config.DbTransportParameter;
import com.tomecode.soa.ora.osb10g.services.config.EndpointASBDB;
import com.tomecode.soa.ora.osb10g.services.config.EndpointASBWLI;
import com.tomecode.soa.ora.osb10g.services.dependnecies.ServiceDependency;

@Service
public class DataService {

	@Autowired
	private ServiceNodeService nodeService;
	
	public ServiceNode parseProxy(OSBService service) throws Exception{
		
		ProxyNode serviceNode =getProxy(service.getOrginalName());

		serviceNode.setBindingRequest(service.getBinding().getRequest());
		serviceNode.setBindingResponse(service.getBinding().getResponse());
		
		serviceNode.setEndPointProtocol(service.getEndpointConfig().getProtocol().name());
		if(service.getEndpointConfig().getUris().size()>0)
			serviceNode.setEndPointUri(service.getEndpointConfig().getUris().get(0));
		
//		serviceNode.setName(service.getOrginalName());
//		serviceNode.setNodeType(service.getType());
		
		prepareDependencies(serviceNode, service);
		
		ProjectNode project = new ProjectNode();
		if(service.getFolder()!=null){
			
			if(service.getFolder().getName().equals("proxy"))
				project  = getProject(service.getProject().getName());
			else
				project  = getProject(service.getFolder().getName());		
			
		}	else{
			project  = getProject(service.getProject().getName());
		}	
		serviceNode.setProject(project);
		return serviceNode;
		
	}
	
	
public ServiceNode parseBusiness(OSBService service) throws Exception{

		BusinessNode serviceNode = getBusiness(service.getOrginalName());
//		if (serviceNode == null)
//			serviceNode = new BusinessNode();

		serviceNode.setBindingRequest(service.getBinding().getRequest());
		serviceNode.setBindingResponse(service.getBinding().getResponse());

		if (service.getEndpointConfig() instanceof EndpointASBDB) {

			EndpointASBDB asbdb = (EndpointASBDB) service.getEndpointConfig();
			serviceNode.getProperties().setProperty("operationType",asbdb.getAsbDbTransportParams().getOperationType());
			serviceNode.getProperties().setProperty("procedureName",asbdb.getAsbDbTransportParams().getProcedureName());
			serviceNode.getProperties().setProperty("sqlStatement",	asbdb.getAsbDbTransportParams().getSqlStatement());

			List<DbTransportParameter> parameterList = asbdb.getAsbDbTransportParams().getParameterList();
			String parameterValues = "";
			for (Iterator iterator = parameterList.iterator(); iterator.hasNext();) {
				DbTransportParameter dbTransportParameter = (DbTransportParameter) iterator.next();
				parameterValues = parameterValues
						+ dbTransportParameter.getParameterName() + "-"
						+ dbTransportParameter.getDirection() + "-"
						+ dbTransportParameter.getSqlType() + "\n";
			}
		}
		
		if(service.getEndpointConfig() instanceof EndpointASBWLI){
			EndpointASBWLI endpointASBWLI = (EndpointASBWLI)service.getEndpointConfig();
			String jpdName = ParseUtil.parseWliEndpoint(endpointASBWLI.getUris());
			ProcessNode process = getProcess(jpdName);
			CallRelationEntity relationEntity = new CallRelationEntity(serviceNode,process,"");
			//serviceNode.getServiceList().add(relationEntity);
			serviceNode.getServiceNodeList().add(process);
			//System.out.println("bitti");
		}
		
		serviceNode.setEndPointProtocol(service.getEndpointConfig().getProtocol().name());
		serviceNode.setEndPointUri(service.getEndpointConfig().getUris().get(0));
//		serviceNode.setName(service.getOrginalName());
//		serviceNode.setNodeType(service.getType());
		
		prepareDependencies(serviceNode, service);
		
		ProjectNode project = new ProjectNode();
		if(service.getFolder()!=null){
			if(service.getFolder().getName().equals("business"))
				project  = getProject(service.getProject().getName());
			else
				project  = getProject(service.getFolder().getName());
			
		}else{
			project  = getProject(service.getProject().getName());
		}
		serviceNode.setProject(project);
		return serviceNode;
		
	}
	

public ProcessNode parseProcess(Process process){
	ProcessNode processNode = getProcess(process.getName()+".jpd");
//    if(processNode == null){
//    	processNode = new ProcessNode();
//    	processNode.setName(process.getName()+".jpd");
//    }
    
	processNode.getProperties().setProperty("returnCodes", process.getReturnCodes());  
    processNode.setProject(getProject(process.getProcessPath()));
    
    Set services = process.getServices();
    for (Iterator iterator = services.iterator(); iterator.hasNext();) {
    	avea.bpm.arch.model.Service service = (avea.bpm.arch.model.Service) iterator.next();
    	ProxyNode serviceNode = getProxy(service.getIwisName());
//		if(serviceNode == null){
//			serviceNode = new ProxyNode();
//			serviceNode.setName(service.getIwisName());
//			nodeService.save(serviceNode);
//		}
		
    	CallRelationEntity relationEntity = new CallRelationEntity(processNode,serviceNode,"");
    	//nodeService.save(relationEntity);
    	System.out.println("rel id = "+relationEntity.getId());
    	//processNode.getServiceList().add(relationEntity);
    	processNode.getServiceNodeList().add(serviceNode);
		
	}
    
   
    
    return processNode;
}

public ProcessNode parseProcess(WliBaseProcess process) throws Exception{
	try {
		ProcessNode processNode = getProcess(process.getName()+".jpd");
//		int i=0;
//
//		if(process.getReturnCodes()!=null)	
//			processNode.getProperties().setProperty("returnCodes", process.getReturnCodes());  
//		//processNode.setProject(getProject(process.getProcessPath()));
//		
//		
//		process.getInput();
//		process.getOutput();
//		CallRelationEntity relationEntity = null;
//		
//		List<WliBaseService> services= process.getWliServices();
//		System.out.println("processName="+process.getName()+",servis sayisi = "+services.size());
//		
//		for (Iterator iterator = services.iterator(); iterator.hasNext();) {
//			
//			
//			WliBaseService service =  (WliBaseService)iterator.next();
//			System.out.println("processName="+process.getName()+",serviceName = "+service.getName());
//			
//			//ProjectNode projectNode = getProject(service.getName());
//			
//			//relationEntity = new CallRelationEntity(processNode,projectNode,service.getOperationName());
//			//nodeService.save(relationEntity);
//			System.out.println("processName="+process.getName()+",serviceName = "+service.getName()+" kaydedildi");
//			//System.out.println("rel id = "+relationEntity.getId());
//			//System.out.println("processNode.getServiceList() size:" + processNode.getServiceList().size());
//			//processNode.getServiceList().add(relationEntity);
//			//processNode.getServiceNodeList().add(projectNode);
//			//System.out.println(" 2 rel size="+nodeService.getAllRelations().size());
//			//System.out.println(processNode.getName() + " " +service.getName()+ " eklendi. " +i++);
//			
//		}
//		
////		for (Iterator iterator = process.getQueneList().keySet().iterator(); iterator.hasNext();) {
////			String methodName = (String) iterator.next();
////			String channelName = process.getQueneList().get(methodName);
////			QueneNode queneNode = getQuene(channelName);
////			relationEntity = new CallRelationEntity(queneNode,processNode,methodName);
////			processNode.getInComingServiceList().add(relationEntity);
////			
////		}
//		
 
		return processNode;
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return null;
}

public ProcessNode parseProcessFromWliWorkspace(WliBaseProcess process){
	ProcessNode processNode = getProcess(process.getName()+".jpd");
	

	if(process.getReturnCodes()!=null)	
		processNode.getProperties().setProperty("returnCodes", process.getReturnCodes());  
    //processNode.setProject(getProject(process.getProcessPath()));
    
    if(process.getName().contains("Ota"))
    	System.out.println("dikkat");
	
    process.getInput();
    process.getOutput();
    
    List<WliBaseService> services= process.getWliServices();
    for (Iterator iterator = services.iterator(); iterator.hasNext();) {
    	WliBaseService service =  (WliBaseService)iterator.next();
    	ServiceNode projectNode = nodeService.findByName(service.getName());
		if(projectNode==null){
			projectNode = nodeService.findByName(service.getName()+"_PS");
			if(projectNode==null)
				continue;
		}
    	
		CallRelationEntity relationEntity = new CallRelationEntity(processNode,projectNode,"");
		//processNode.getServiceList().add(relationEntity);
		processNode.getServiceNodeList().add(projectNode);
		
	}
    
    nodeService.save(processNode);
    return processNode;
}

public ProjectNode getProject(String name){
	ProjectNode project  = nodeService.getProject(name);
	if(project==null){
		project = new ProjectNode();
		project.setName(name);
		nodeService.save(project);
	}
	
	return project;
}

public QueneNode getQuene(String name){
	QueneNode queneNode  = nodeService.getQueneNode(name);
	if(queneNode==null){
		queneNode = new QueneNode();
		queneNode.setName(name);
		nodeService.save(queneNode);
	}
	return queneNode;
}


public ProcessNode getProcess(String name){
	ProcessNode process  = nodeService.getProcessNode(name);
	if(process==null){
		process = new ProcessNode();
		process.setName(name);
		nodeService.save(process);
	}
	return process;
}

public ProxyNode getProxy(String name){
	ProxyNode proxy  = nodeService.getProxyNode(name);
	if(proxy==null){
		proxy = new ProxyNode();
		proxy.setName(name);
		nodeService.save(proxy);
	}
	
	return proxy;
}

public BusinessNode getBusiness(String name){
	BusinessNode business  = nodeService.getBusinessNode(name);
	if(business==null){
		business = new BusinessNode();
		business.setName(name);
		nodeService.save(business);
	}
	
	return business;
}


public void prepareDependencies(ServiceNode serviceNode,OSBService service) throws Exception{
	if(service.getName().equals("POSTPAID_EBILL_REGISTER_FOR_GIB_PS"))
		System.out.println("dikkat");
	System.out.println("dependency list ="+service.getName());
	
	List<ServiceDependency> list = service.getServiceDependencies().getDependnecies();
	ServiceNode targetNode = null;
//	if(serviceNode.getName().equals("BPM_PRE_MSISDN_CHANGE_PS"))
//		System.out.println("geldi");
	
	for (Iterator iterator = list.iterator(); iterator.hasNext();) {
		ServiceDependency serviceDependency = (ServiceDependency) iterator.next();
		
		if(serviceDependency.getType().toString().contains("WLI")){
			for (Iterator iterator2 = serviceDependency.getServices().iterator(); iterator2.hasNext();) {
				WliBaseProcess process = (WliBaseProcess) iterator2.next();
				targetNode = parseProcess(process);
				
			}
		
		}else if(serviceDependency.getType().toString().contains("Proxy")){
			for (Iterator iterator2 = serviceDependency.getServices().iterator(); iterator2.hasNext();) {
				Object object = iterator2.next();
				if(object instanceof Proxy){
				Proxy process = (Proxy)object;
				targetNode = parseProxy(process);
				
				
				}
			}

		}else if(serviceDependency.getType().toString().contains("Business")){
			
			for (Iterator iterator2 = serviceDependency.getServices().iterator(); iterator2.hasNext();) {
				Object object = iterator2.next();
				if(object instanceof BusinessService){
					BusinessService process = (BusinessService) object;
					targetNode = parseBusiness(process);
				}
			
			}
		
		}
		
		
		
		
//		String targetNodeName = serviceDependency.getServiceName();
//		ServiceNode targetNode =  nodeService.getServiceNode(targetNodeName);
//		if(targetNode==null){
//			if(serviceDependency.getType().toString().contains("Business"))
//				targetNode = getBusiness(targetNodeName);
//			else if(serviceDependency.getType().toString().contains("Proxy"))	
//				targetNode = getProxy(targetNodeName);
//			else if(serviceDependency.getType().toString().contains("WLI"))
//				targetNode= getProcess(targetNodeName);
////			nodeService.save(targetNode);
//		}
		
		String operationName = getOperationName(service)	;
	serviceNode.getServiceNodeList().add(targetNode);
	CallRelationEntity relationEntity = new CallRelationEntity(serviceNode,targetNode,operationName);
	//serviceNode.getServiceList().add(relationEntity);
	}
	
}

public String getOperationName(OSBService osbService){
	
	if(osbService.getType().  equals("Proxy Service")){
		return osbService.getBinding().getWsdlOperation();
		
	}
	
	if(osbService.getType().equals("Business Service")){
		System.out.println("dsds");
		return "";
	}
	return "";
}

}

