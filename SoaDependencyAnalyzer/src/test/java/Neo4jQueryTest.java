

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import avea.bpm.arch.model.Process;
import avea.bpm.codeanalyzer.runner.CoreRunner;

import com.avea.dependencyanalyzer.model.ProcessNode;
import com.avea.dependencyanalyzer.model.ServiceNode;
import com.avea.dependencyanalyzer.service.DataService;
import com.avea.dependencyanalyzer.service.ServiceNodeService;
import com.tomecode.soa.ora.osb10g.parser.OraSB10gMWorkspaceParser;
import com.tomecode.soa.ora.osb10g.project.OraSB10gProject;
import com.tomecode.soa.ora.osb10g.services.OSBService;
import com.tomecode.soa.ora.osb10g.services.ResourceFile;
import com.tomecode.soa.ora.osb10g.workspace.OraSB10gMultiWorkspace;
import com.tomecode.soa.project.Project;
import com.tomecode.soa.workspace.Workspace;

@ContextConfiguration(locations = "classpath:helloWorldContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class Neo4jQueryTest {

	@Autowired
	private ServiceNodeService nodeService;
	
	@Autowired
	private DataService dataService;

	
	@Autowired
	private Neo4jTemplate template;
	
	@Rollback(false)
	@BeforeTransaction
	public void cleanUpGraph() {
		//Neo4jHelper.cleanDb(template);
//		nodeService.deleteAll();
//		ServiceNode node2 = new ServiceNode("dummy-2");
//		nodeService.save(node2);
	}
	
	
	
	@Rollback(false)
	//@Ignore
	@Test
    public void parseWli() throws Exception{
	
	
		CoreRunner runner = new CoreRunner();
	try {
		Map<String,List> result = runner.run("D:/mw/workspaces/osb1030/soa-analizer-test-ws", 
				"D:/mw/workspaces/osb1030/soa-analizer-test-ws/BPMBase/IWIS_Config.xml");
//		
		for (Iterator iterator = result.keySet().iterator(); iterator.hasNext();) {
			String projectName = (String) iterator.next();
	
			List list  =result.get(projectName);
			
			for (Iterator iterator2 = list.iterator(); iterator2.hasNext();) {
				Process process = (Process) iterator2.next();
				ProcessNode processNode= dataService.parseProcess(process);
				nodeService.save(processNode);
			}
			
		}
		
		
		System.out.println("bitti");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	
	@Rollback(false)
	//@Ignore
	@Test
    public void parseServices() throws Exception{

	try {

//		nodeService.deleteAll();
//		ServiceNode node2 = new ServiceNode("dummy-2");
//		nodeService.save(node2);
		
	//	template.
		
		System.out.println("baliyor");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			
			OraSB10gMWorkspaceParser oraSB10gParser = new OraSB10gMWorkspaceParser();;
			OraSB10gMultiWorkspace multiWorkspace = new OraSB10gMultiWorkspace("MWS", new File("C:"));
			
			List<String> path  = new ArrayList<String>();
			path.add("D:/mw/workspaces/osb1030/osb-new-test-2");
			
			oraSB10gParser.parse(multiWorkspace, path);
			Workspace ws = multiWorkspace.getWorkspaces().get(0);
			
			
			List<Project> list  =  ws.getProjects();
			
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				OraSB10gProject project = (OraSB10gProject) iterator.next();
				System.out.println(project.getName());
				
				
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
		
				if (osbService.getProject().getName().equals("TAXIM_CAMPAIGN_OPERATIONS")) {
					System.out.println("dikkat");
				}	
					nodeService.save(node);
				}
				
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
    }
	
	
	
	
}
