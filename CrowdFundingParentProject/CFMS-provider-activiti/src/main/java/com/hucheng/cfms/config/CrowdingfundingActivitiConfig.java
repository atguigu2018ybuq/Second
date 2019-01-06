package com.hucheng.cfms.config;

import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CrowdingfundingActivitiConfig implements ProcessEngineConfigurationConfigurer {

	@Override
	public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
		processEngineConfiguration.setActivityFontName("宋体");
		processEngineConfiguration.setLabelFontName("宋体");

		// 设置邮箱服务器的主机地址
		String mailServerHost = "192.168.163.211";
		processEngineConfiguration.setMailServerHost(mailServerHost);
		
		//设置邮件服务器的端口号
		int mailServerPort =25;
		processEngineConfiguration.setMailServerPort(mailServerPort);

	}

}
