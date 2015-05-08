

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Relationship;
import org.neo4j.rest.graphdb.entity.RestNode;
import org.neo4j.rest.graphdb.entity.RestRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.avea.dependencyanalyzer.model.ProcessNode;
import com.avea.dependencyanalyzer.model.ProjectNode;
import com.avea.dependencyanalyzer.model.ServiceNode;
import com.avea.dependencyanalyzer.repository.ServiceNodeRepository;
import com.avea.dependencyanalyzer.service.GraphDBService;
import com.avea.dependencyanalyzer.service.ServiceNodeService;
import com.tomecode.soa.beawli.workspace.WliBaseMultiWorkspace;
import com.tomecode.soa.beawli.workspace.WliBaseWorkspace;
import com.tomecode.soa.ora.osb10g.workspace.OraSB10gMultiWorkspace;
import com.tomecode.soa.project.Project;
import com.tomecode.soa.workspace.MultiWorkspace;

@ContextConfiguration(locations = "classpath:helloWorldContext_local.xml")
//@ContextConfiguration(locations = "file:///D:/calismalar/htmlslide/SoaDependencyAnalyzer/src/main/resources/helloWorldContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RemoteWorkspaceWliFromBean {

	

	@Autowired
	private GraphDBService graphDbService;
	
	@Autowired
	private ServiceNodeService nodeService;
	
	@Autowired(required=true)
	Neo4jTemplate neo4jTemplate;
	
	@Autowired(required=true)
	private ServiceNodeRepository serviceNodeRepository;
	
	@Rollback(false)
	@Ignore
	@Test
    public void delete() throws Exception{
		nodeService.deleteAll();
		System.out.println("delete all");
	}
	
	@Rollback(false)
	@Ignore
	@Test
    public void updateProcess() throws Exception{
		//nodeService.deleteAll();
		graphDbService.updateDB("D:/calismalar/ws/ws_test/");
		ProcessNode node =nodeService.getProcessNode("ExpiringOrderNotify.jpd");
		System.out.println(node.getName());
	}
		
	
	@Rollback(false)
	@Ignore
	@Test
    public void updateProcessTest() throws Exception{
		//nodeService.deleteAll();
		List<MultiWorkspace> multiWorkspaces = graphDbService.getMultiWorkspace("D:/calismalar/ws/ws/");
		OraSB10gMultiWorkspace multiWorkspaceASB= (OraSB10gMultiWorkspace)multiWorkspaces.get(0);
		WliBaseMultiWorkspace wliMultiWorkspace = (WliBaseMultiWorkspace)multiWorkspaces.get(2);
		
		
		WliBaseWorkspace mw = (WliBaseWorkspace) wliMultiWorkspace.getWorkspaces().get(0);
		List<Project> projectList = mw.getProjects();
		
		int katsayi = 10;
		int kalan = projectList.size()%katsayi;
		int total = projectList.size() - kalan;
		int grupSayisi = total / katsayi;
		
		
		for (int i = 1; i <katsayi; i++) {
			List<Project> sublist  = projectList.subList(i*grupSayisi, (i+1)*grupSayisi);
			graphDbService.updateDBForProcesses(sublist);
		}
		
		List<Project> lastList  = projectList.subList(katsayi*grupSayisi,projectList.size()-katsayi*grupSayisi );
		graphDbService.updateDBForProcesses(lastList);

		
		
		
	}
		
	
	
	
	@Rollback(false)
	//@Ignore
	@Test
    public void updateServices() throws Exception{
		System.out.println("start date = "+new Date());
     	//nodeService.deleteAll();
		System.out.println("delete all");
		graphDbService.updateDB("D:/calismalar/ws/ws/");
		System.out.println("end date = "+new Date());
	}
	
	@Rollback(false)
	@Ignore
	@Test
    public void search() throws Exception{
		
		List<ServiceNode> list = nodeService.searchByName("RBT_PLATFORM_OPERATIONS");
		
		ServiceNode node =list.get(0);
		ProjectNode projectNode = null;
		
//		if(node instanceof ProjectNode){
//			projectNode =  (ProjectNode)node;
			
		//System.out.println(projectNode.getContainsList().size());
		
		for (Iterator iterator = node.getContainsList().iterator(); iterator.hasNext();) {
//			CallRelationEntity relation = (CallRelationEntity) iterator.next();
//			relation.getEndNode();
//			relation.getStartNode();
//			relation.getMethodName();
			
			ServiceNode  node2 = (ServiceNode)iterator.next();
			System.out.println(node2.getName());
			
		}
		
		System.out.println(list.size());
	}
		
	//}
	
	@Rollback(true)
	@Ignore
	@Test
    public void neo4jTemplateTest() throws Exception{
		
//		System.out.println(new Date());
//		ServiceNode serviceNodex= nodeService.findWorldById(new Long(35200));
//		System.out.println(new Date());
//		
//		List<ServiceNode> asdasd = nodeService.searchByNameByIndex("BPM_GNL_INT_PACKAGE_DEACTV_BS");
		
		System.out.println(new Date());
		Result<Map<String,Object>> resultsss  = neo4jTemplate.query("START root=node(*)  WHERE root.name ='BPM_GNL_INT_PACKAGE_DEACTV_BS' RETURN root  ", null);
		
		for (Iterator iterator = resultsss.iterator(); iterator.hasNext();) {
			HashMap<String , Object> map = ( HashMap<String , Object>)iterator.next();
			for (Iterator iterator2 = map.keySet().iterator(); iterator2.hasNext();) {
				String type = (String) iterator2.next();
				RestNode node = (RestNode)map.get(type);
				//node.get
				//ServiceNode serviceNode= findWorldById(node.getId());
				Iterable<Relationship> il=	node.getRelationships(Direction.OUTGOING);
				while (il.iterator().hasNext()) {
					RestRelationship object = (RestRelationship) il.iterator().next();
					System.out.println(object);
					object.getEndNode().getId();
					
					object.getType().name();
				}
				
				
				//resultList.add(serviceNode);
				System.out.println("test");
				
			}
		
		} 
		
		
		System.out.println(new Date());
		
		
		EndResult<ServiceNode> avvv = serviceNodeRepository.findAllByPropertyValue("name", "BPM_GNL_INT_PACKAGE_DEACTV_BS");
		
		Iterator it = avvv.iterator();
		
		while (it.hasNext()) {
			ServiceNode type = (ServiceNode) it.next();
			System.out.println();
			
		}
		
		System.out.println(new Date());
		Result<Map<String,Object>> results  = neo4jTemplate.query("START root=node(*)  WHERE root.name ='BPM_GNL_INT_PACKAGE_DEACTV_BS' RETURN root  ", null);
		System.out.println("test");
		for (Iterator iterator = results.iterator(); iterator.hasNext();) {
			HashMap<String , Object> map = ( HashMap<String , Object>)iterator.next();
			for (Iterator iterator2 = map.keySet().iterator(); iterator2.hasNext();) {
				String type = (String) iterator2.next();
				RestNode node = (RestNode)map.get(type);node.getRelationships();
				//ServiceNode serviceNode = (ServiceNode)node;
				ServiceNode serviceNode= nodeService.findWorldById(node.getId());
				//List<ServiceNode> list  =nodeService.searchByNameByIndex("BPM_GNL_INT_PACKAGE_DEACTV_BS");
				System.out.println("test it");
			
				
				
			}
		
		} 
		
		
		System.out.println(new Date());
		int a =nodeService.searchByNameByIndex("BPM_GNL_INT_PACKAGE_DEACTV_BS").size();
		System.out.println(a);
		System.out.println(new Date());
//		
//		Iterable<ServiceNode> list = nodeService.findAllByName("RBT_PLATFORM_OPERATIONS");
//		Iterator< ServiceNode> it =list.iterator();
//		while(it.hasNext()){
//			System.out.println(it.next().getId());
//		}
	}
	
			
	
}
