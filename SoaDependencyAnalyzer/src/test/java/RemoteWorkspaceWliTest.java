

import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.avea.dependencyanalyzer.model.ServiceNode;
import com.avea.dependencyanalyzer.service.DataService;
import com.avea.dependencyanalyzer.service.ServiceNodeService;
import com.tomecode.soa.beawli.project.WliBaseProject;
import com.tomecode.soa.beawli.services.WliBaseProcess;
import com.tomecode.soa.beawli.services.WliBaseService;
import com.tomecode.soa.beawli.workspace.WliBaseMultiWorkspace;
import com.tomecode.soa.beawli.workspace.WliBaseWorkspace;
import com.tomecode.soa.dependency.analyzer.core.ApplicationManager;
import com.tomecode.soa.ora.osb10g.workspace.OraSB10gMultiWorkspace;
import com.tomecode.soa.workspace.MultiWorkspace;

@ContextConfiguration(locations = "classpath:helloWorldContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RemoteWorkspaceWliTest {

	

	@Autowired
	private ServiceNodeService nodeService;
	
	@Autowired
	private DataService dataService;

	
	@Autowired
	private Neo4jTemplate template;
	
	
	
	private List<MultiWorkspace> multiWorkspaces = null;
	
	private static ApplicationManager appManager = null;
	
	@Rollback(false)
	//@Ignore
	@Test
    public void se() throws Exception{
	
		
		
		
	try {
		nodeService.deleteAll();

		List<MultiWorkspace> multiWorkspaces = getRemoteMultiWorkspace("D:/calismalar/ws/ws_test/");
		OraSB10gMultiWorkspace multiWorkspaceASB= (OraSB10gMultiWorkspace)multiWorkspaces.get(0);
		WliBaseMultiWorkspace wliMultiWorkspace = (WliBaseMultiWorkspace)multiWorkspaces.get(2);
		
		WliBaseWorkspace mw = (WliBaseWorkspace) wliMultiWorkspace.getWorkspaces().get(0);
		
		for(int i=0;i<mw.getProjects().size();i++){
		WliBaseProject	 p= (WliBaseProject)mw.getProjects().get(i);
//			System.out.println(p.getName());
//			if(p.getName().contains("Ota"))
//				System.out.println("dikkat");
		
			ServiceNode serviceNode= nodeService.findByName(p.getName()+".jpd");
			System.out.println(p.getName());
			if(p.getName().equals("BroadcastSms"))
				System.out.println("dikkat");
			if(serviceNode==null){
				if(p.getProcessList().size()==0)
					continue;
				WliBaseProcess process= p.getProcessList().get(0);
				List<WliBaseService> list = process.getWliServices();
				if(list.size()==0)
					continue;
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					WliBaseService wliBaseService = (WliBaseService) iterator.next();
					System.out.println(p.getName() +"-"+wliBaseService);
					ServiceNode node= nodeService.findByName(wliBaseService.getName());
					if(node == null){
						System.out.println(p.getName() +"-"+wliBaseService+" bulunamadı" );
						node= nodeService.findByName(wliBaseService.getName()+"_PS");
						if(node == null)
							System.out.println(p.getName() +"-"+wliBaseService+" hala bulunamadı" );
					}
//					else
//						System.out.println(p.getName() +"-"+wliBaseService+" bulundu" );
				}
				
				
				
				//System.out.println(p.getName() +" bulunamadı" );
			}	
//			else
//				System.out.println(p.getName() +" bulunamadı" );
		
		}
		System.out.println("bitti");
	
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	
	
	public List<MultiWorkspace> getRemoteMultiWorkspace(String path) {
//		String url = System.getProperty("soaAnalizerUrl"); // getUrlFromEclipseIni();
//		url = "http://10.248.68.88:8080/SoaAnalyzerWeb/HessianMultiWsServlet";
//		HessianProxyFactory factory = new HessianProxyFactory();
//		try {
//			final MultiWorkspaceService multiWsService = (MultiWorkspaceService) factory.create(MultiWorkspaceService.class, url);
//			return multiWsService.getMultiWorkspaces();
//			
//			
//			
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		 if(appManager==null)
	            appManager = ApplicationManager.getWebInstance();

			appManager.setWorkspacePath(path);
			appManager.initialize();  
			
			return appManager.getMultiWorkspaces();
		//return null;
	}
}
