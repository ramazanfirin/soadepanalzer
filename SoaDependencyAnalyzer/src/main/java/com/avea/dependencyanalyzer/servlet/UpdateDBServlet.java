package com.avea.dependencyanalyzer.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.avea.dependencyanalyzer.service.GraphDBService;

/**
 * Servlet implementation class UpdateDBServlet
 */
public class UpdateDBServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	  @Autowired
	  private GraphDBService graphDBService;
	
	  
	  @Override
	    public void init(ServletConfig config) throws ServletException {
	        super.init(config);
	    }
    /**
     * Default constructor. 
     */
    public UpdateDBServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			System.out.println("parsing basliyor");
			ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
			// WebApplicationContext springContext = 			        WebApplicationContextUtils.getWebApplicationContext(getServletContext());
			GraphDBService graphDBService = (GraphDBService)context.getBean("graphDBService");//graphDBService.getRemoteMultiWorkspace();getServletContext().getAttribute("graphDBService");
			graphDBService.updateDB(getServletContext().getInitParameter("workspacePath"));
			System.out.println("parsing basliyor");
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
