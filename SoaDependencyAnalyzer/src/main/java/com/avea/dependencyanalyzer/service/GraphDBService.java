package com.avea.dependencyanalyzer.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avea.dependencyanalyzer.model.ProcessNode;
import com.avea.dependencyanalyzer.model.ServiceNode;
import com.tomecode.soa.beawli.project.WliBaseProject;
import com.tomecode.soa.beawli.services.WliBaseProcess;
import com.tomecode.soa.beawli.workspace.WliBaseMultiWorkspace;
import com.tomecode.soa.beawli.workspace.WliBaseWorkspace;
import com.tomecode.soa.dependency.analyzer.core.ApplicationManager;
import com.tomecode.soa.ora.osb10g.project.OraSB10gProject;
import com.tomecode.soa.ora.osb10g.services.OSBService;
import com.tomecode.soa.ora.osb10g.services.ResourceFile;
import com.tomecode.soa.ora.osb10g.workspace.OraSB10gMultiWorkspace;
import com.tomecode.soa.project.Project;
import com.tomecode.soa.workspace.MultiWorkspace;
//import com.tomecode.soa.dependency.analyzer.core.ApplicationManager;
import com.tomecode.soa.workspace.Workspace;

@Service
public class GraphDBService {
	
	@Autowired(required=true)
	private ServiceNodeService nodeService;
	
	@Autowired
	private DataService dataService;
	
	private static ApplicationManager appManager = null;
	
	
	public void updateDB(String workspacePath) throws Exception{
		
		
		List<MultiWorkspace> multiWorkspaces = getMultiWorkspace(workspacePath);
		OraSB10gMultiWorkspace multiWorkspaceASB= (OraSB10gMultiWorkspace)multiWorkspaces.get(0);
		WliBaseMultiWorkspace wliMultiWorkspace = (WliBaseMultiWorkspace)multiWorkspaces.get(2);
		
		//nodeService.deleteAll();
		//System.out.println("tum kayıtlar silindi");
		
		WliBaseWorkspace mw = (WliBaseWorkspace) wliMultiWorkspace.getWorkspaces().get(0);
		updateDBForServices(multiWorkspaceASB);
		updateDBForProcesses(mw.getProjects());
		
	}
	
	
	public void updateDBForProcesses(List<Project> projectList){
		try {
			
			
			
		
			
			for(int i=0;i<projectList.size();i++){
				WliBaseProject	 p= (WliBaseProject)projectList.get(i);
				
				for(int j=0;j<p.getProcessList().size();j++){
						WliBaseProcess process= p.getProcessList().get(j);
						try {
							System.out.println(process.getName() + ".jpd parse basladi.");
							String processName  = nodeService.getProjectName(process.getName()+".jpd");
							if(processName!=null){
								System.out.println(process.getName() + ".jpd  bulundu");
								continue;
							}else{
								System.out.println(process.getName() + ".jpd  bulunamadi");
							}
							ProcessNode processNode= dataService.parseProcess(process);
							//ProcessNode processNode=new ProcessNode();
							System.out.println(processNode.getName() + " parse edildi");
							nodeService.save(processNode);
						} catch (Exception e) {
							System.out.println(process.getName() + " parse ederken hata olustu");
							e.printStackTrace();
						}
						//System.out.println(" 1 rel size="+nodeService.getAllRelations().size());
				}
					
			}
		//System.out.println(" 3 rel size="+nodeService.getAllRelations().size());
			//System.out.println("process parsing bitti");
		
		} catch (RuntimeException e) {
			System.out.println("process parse hata olustu");
			e.printStackTrace();
		}
		
		System.out.println("process parsing bitti="+new Date());
		}
	
	public void updateDBForServices(OraSB10gMultiWorkspace multiWorkspaceASB) throws Exception{
		
	
		
		
		Workspace ws = multiWorkspaceASB.getWorkspaces().get(0);
		
		
		List<Project> list  =  ws.getProjects();
		
		System.out.println("update baslıyor");
//		int minCounter = 0;
//		int maxCounter = 50;
//		int currentCounter=0;
		
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			OraSB10gProject project = (OraSB10gProject) iterator.next();
			System.out.println(project.getName());
			//currentCounter++;
//			
//			if(currentCounter<minCounter)
//				continue;
//			if(currentCounter>maxCounter)
//				break;
			
			List<OSBService> osbServices = project.getServices();
			ServiceNode node=null;
			int count=0;
			for (Iterator iterator2 = osbServices.iterator(); iterator2.hasNext();) {
			
				OSBService osbService = (OSBService) iterator2.next();
				System.out.println(++count+" "+osbService.getOrginalName());
								
				if (osbService instanceof ResourceFile) {
					continue;
				}
				
				if(osbService.getType().equals("Unknown"))
					continue;
				
				if(osbService.getOrginalName().equals("POSTPAID_CHECK_SIMCARD_ACTIVATION"))
					System.out.println("dikkat");
				
				if(osbService.getType().  equals("Proxy Service")){
					if(!osbService.getOrginalName().contains("_PS"))
						continue;
					node = dataService.parseProxy(osbService);	
				}
				
				if(osbService.getType().equals("Business Service")){
					 node = dataService.parseBusiness(osbService);		
				}
	
			if (osbService.getProject().getName().contains("POSTPAID_EBILL_REGISTER")) {
				System.out.println("dikkat");
			}	
				nodeService.save(node);
			}
			
		
		
		System.out.println("bitti");
		}
	}
	
	
//	public List<MultiWorkspace> getRemoteMultiWorkspace() {
//		String url = System.getProperty("soaAnalizerUrl"); // getUrlFromEclipseIni();
//		url = "http://10.248.68.88:8080/SoaAnalyzerWeb/HessianMultiWsServlet";
//		HessianProxyFactory factory = new HessianProxyFactory();
//		try {
//			final MultiWorkspaceService multiWsService = (MultiWorkspaceService) factory.create(MultiWorkspaceService.class, url);
//			List<MultiWorkspace> list = multiWsService.getMultiWorkspaces();
//			System.out.println(list.size());
//			return list;
//			
//			
//			
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	public List<MultiWorkspace> getMultiWorkspace(String path) {
        if(appManager==null)
            appManager = ApplicationManager.getWebInstance();

		appManager.setWorkspacePath(path);
		appManager.initialize();  
		
		return appManager.getMultiWorkspaces();

	}
}
