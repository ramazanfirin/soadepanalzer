

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Node;
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

import com.avea.dependencyanalyzer.model.CallRelationEntity;
import com.avea.dependencyanalyzer.model.ProjectNode;
import com.avea.dependencyanalyzer.model.ServiceNode;
import com.avea.dependencyanalyzer.repository.ServiceNodeRepository;
import com.avea.dependencyanalyzer.service.GraphDBService;
import com.avea.dependencyanalyzer.service.ServiceNodeService;
import com.avea.dependencyanalyzer.util.ConvertUtil;

@ContextConfiguration(locations = "classpath:helloWorldContext.xml")
//@ContextConfiguration(locations = "file:///D:/calismalar/htmlslide/SoaDependencyAnalyzer/src/main/resources/helloWorldContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RemoteWorkspaceQuery {

	

	@Autowired
	private GraphDBService graphDbService;
	
	@Autowired
	private ServiceNodeService nodeService;
	
	@Autowired(required=true)
	Neo4jTemplate neo4jTemplate;
	
	@Autowired(required=true)
	private ServiceNodeRepository serviceNodeRepository;
	
	
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
		Node abc=neo4jTemplate.getNode(35200);
		serviceNodeRepository.findOne(new Long(35200));
		System.out.println(new Date());
		Result<Map<String,Object>> resultsss  = neo4jTemplate.query("START root=node(*)  WHERE root.name ='BPM_GNL_INT_PACKAGE_DEACTV_BS' RETURN root  ", null);
		
		for (Iterator iterator = resultsss.iterator(); iterator.hasNext();) {
			HashMap<String , Object> map = ( HashMap<String , Object>)iterator.next();
			for (Iterator iterator2 = map.keySet().iterator(); iterator2.hasNext();) {
				String type = (String) iterator2.next();
				RestNode node = (RestNode)map.get(type);
				
//				neo4jTemplate.createNodeAs(ServiceNode.class,node.get);
//				
//				ServiceNode nodesd = new ServiceNode() {
//					
//					@Override
//					public String getType() {
//						// TODO Auto-generated method stub
//						return null;
//					}
//				};
				
				
				
				
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
				
				ServiceNode serviceNodeaaa = neo4jTemplate.createEntityFromState(node, ServiceNode.class, neo4jTemplate.getMappingPolicy(ServiceNode.class));
				
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
	
			
	@Rollback(true)
	//@Ignore
	@Test
    public void neo4jTemplateTest2() throws Exception{
		System.out.println(new Date());
		//nodeService.findRelation(new Long(42211));
		
		
		
//		System.out.println(new Date());
//		List<ServiceNode> list  =nodeService.searchByNameByIndex("BPM_GNL_INT_PACKAGE_DEACTV_BS");//BPM_POST_PSTN_PACKAGE_ACTIVATION_VLD
		

				RestNode node = nodeService.searchByNativeQuery("VASPROMO_PASSWORD_CHECK_BS");//PREPAID_CHANGE_PRF_COMMIT_BS
				ServiceNode root=nodeService.convertToServiceNode(node);
				System.out.println(new Date());
				
				
				
				
				//serviceNodeaaa = neo4jTemplate.createEntityFromState(node, ServiceNode.class, neo4jTemplate.getMappingPolicy(ServiceNode.class) );
				
			
				//
				System.out.println("test it");

		
		System.out.println(new Date());
		System.out.println("bitti");
	}
	
}
