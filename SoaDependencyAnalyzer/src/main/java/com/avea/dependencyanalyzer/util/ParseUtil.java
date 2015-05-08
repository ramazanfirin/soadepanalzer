package com.avea.dependencyanalyzer.util;

import java.util.List;

public class ParseUtil {

	public static void main(String[] args) {
		//System.out.println(parseWliEndpoint("//bpm:BPMJNDIProvider:/BppsOrderNotifications/avea/bpm/processes/BppsAPOrderAct.jpd"));
	}
	
public static String parseWliEndpoint(List list){
	
	if(list == null || list.size()==0)
		return "";
	
	String path=(String)list.get(0);
	
	String[] array = path.split("/");
	
	return array[array.length-1];

}
	
}
