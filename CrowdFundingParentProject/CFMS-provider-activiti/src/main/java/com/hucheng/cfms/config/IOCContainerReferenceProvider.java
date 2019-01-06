package com.hucheng.cfms.config;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Component
public class IOCContainerReferenceProvider implements ServletContextAware {
	
	private static ServletContext servletContext;
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		IOCContainerReferenceProvider.servletContext = servletContext;
	}
	
	public static WebApplicationContext getIOCContainerReference() {
		return WebApplicationContextUtils.getWebApplicationContext(servletContext);
	}
}
