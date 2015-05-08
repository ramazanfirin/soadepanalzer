package com.avea.dependencyanalyzer.gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.mindmap.DefaultMindmapNode;
import org.primefaces.model.mindmap.MindmapNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.avea.dependencyanalyzer.model.CallRelationEntity;
import com.avea.dependencyanalyzer.model.ServiceNode;
import com.avea.dependencyanalyzer.service.GraphDBService;
import com.avea.dependencyanalyzer.service.ServiceNodeService;

@Component
@ManagedBean(name="userBean")
@SessionScoped
public class UserBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired(required=true)
	GraphDBService graphDBService;
	
	@Autowired(required=true)
	ServiceNodeService nodeService;
	
	String searchText;
	
	List<ServiceNode> searchList = new ArrayList<ServiceNode>();
	
	ServiceNode selectedNode;
	
	private MindmapNode mindMaproot = new DefaultMindmapNode("root", "", "FF0000", true);  //kirmizi;  
    private MindmapNode mindMapselectedNode;
    
    ServiceNode serviceNodeForMindMap;
    String serviceNameForMindMap;    
	
	boolean renderMindMap = false;

	
	public String test(){
		System.out.println("test");
		
		return "";
	}
	
	public String search(){
		
		System.out.println(new Date());
        //searchList = nodeService.searchByNameByIndex(searchText);//index ile arama, rest le yavaş çalışıyor
		searchList = new ArrayList<ServiceNode>();
		searchList.add(nodeService.getServiceCodeByNativeQuery(searchText));
		System.out.println(new Date());
		return "";
	}
	
	public String drawGraph(){
		
		mindMaproot.getChildren().clear();
		MindmapNode root = new DefaultMindmapNode(selectedNode.getName(), selectedNode, "FF0000", true);
		mindMaproot.addNode(root);

		renderMindMap = true;
		return "";
	}
	
	public List<String> complete(String query) {  
		return nodeService.searchName(query);
    }  
	
	public void prepareRootMindMap(MindmapNode root){

		ServiceNode serviceNode = (ServiceNode)root.getData();
		root.getChildren().clear();
		for (Iterator iterator = serviceNode.getContainsAsList().iterator(); iterator.hasNext();) {
			ServiceNode node = (ServiceNode) iterator.next();
			root.addNode(new DefaultMindmapNode(node.getName(), node, "FFFF00",true) ); //sari
		}

//		for (Iterator iterator = serviceNode.getIncomingServicesAsList().iterator(); iterator.hasNext();) {
//			CallRelationEntity relation = (CallRelationEntity) iterator.next();
//			root.addNode(new DefaultMindmapNode(relation.getStartNode().getName(), relation.getStartNode(), "00FF00",true) );//yesil
//		}
//		
//		for (Iterator iterator = serviceNode.getOutgoingServicesAsList().iterator(); iterator.hasNext();) {
//			CallRelationEntity relation = (CallRelationEntity) iterator.next();
//			root.addNode(new DefaultMindmapNode(relation.getEndNode().getName(), relation.getEndNode(), "0000FF",true) ); //mavi
//		}
		
		if(serviceNode.getProject()!=null){
			root.addNode(new DefaultMindmapNode(serviceNode.getProject().getName(), serviceNode.getProject(), "808080",true) );//gri
		}
		
		//mindMaproot.addNode(mindNode);
	}
	
	public UserBean() {
		super();
		// TODO Auto-generated constructor stub
		
	}

	public void onNodeSelect(SelectEvent event) {  
        MindmapNode node = (MindmapNode) event.getObject();  
          
        //mindMaproot = new DefaultMindmapNode(serviceNameForMindMap, serviceNodeForMindMap, "FF0000", true);  
        System.out.println(node.getLabel());
        prepareRootMindMap(node);
          
    }  
	
	public void onNodeDblselect(SelectEvent event) {  
		MindmapNode mindmapNode =  (MindmapNode) event.getObject();          
		selectedNode =(ServiceNode) mindmapNode.getData();
	}  
	
	
	 public void chooseNode() {  
         RequestContext.getCurrentInstance().openDialog("detailsaAAA");  
     }  
	/////////Getter - Setter
	
	public MindmapNode getMindMaproot() {
		return mindMaproot;
	}

	public void setMindMaproot(MindmapNode mindMaproot) {
		this.mindMaproot = mindMaproot;
	}

	public MindmapNode getMindMapselectedNode() {
		return mindMapselectedNode;
	}

	public void setMindMapselectedNode(MindmapNode mindMapselectedNode) {
		this.mindMapselectedNode = mindMapselectedNode;
	}	
	
	public ServiceNode getSelectedNode() {
		return selectedNode;
	}


	public void setSelectedNode(ServiceNode selectedNode) {
		this.selectedNode = selectedNode;
	}
	
	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public List<ServiceNode> getSearchList() {
		return searchList;
	}

	public void setSearchList(List<ServiceNode> searchList) {
		this.searchList = searchList;
	}

	public GraphDBService getGraphDBService() {
		return graphDBService;
	}

	public void setGraphDBService(GraphDBService graphDBService) {
		this.graphDBService = graphDBService;
	}

	public boolean getRenderMindMap() {
		return renderMindMap;
	}

	public void setRenderMindMap(boolean renderMindMap) {
		this.renderMindMap = renderMindMap;
	}
}
