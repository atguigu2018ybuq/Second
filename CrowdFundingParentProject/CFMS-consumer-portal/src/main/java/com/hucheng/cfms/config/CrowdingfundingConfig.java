package com.hucheng.cfms.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class CrowdingfundingConfig extends WebMvcConfigurerAdapter {

	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// 前往用户的主页面
		registry.addViewController("/user/main").setViewName("management/main");
		// 前往会员的主页面
		registry.addViewController("/member/main").setViewName("member/main");
		// 前往注册页面
		registry.addViewController("/member/register").setViewName("info/register");
		// 前往登录页面
		registry.addViewController("/member/login").setViewName("info/login");
		// 前往首页
		registry.addViewController("/index").setViewName("main");
	}
}
