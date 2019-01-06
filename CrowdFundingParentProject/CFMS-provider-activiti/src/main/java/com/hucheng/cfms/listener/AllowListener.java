package com.hucheng.cfms.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.web.context.WebApplicationContext;

import com.hucheng.cfms.api.DatabaseProviderRemoteService;
import com.hucheng.cfms.config.IOCContainerReferenceProvider;

public class AllowListener implements ExecutionListener {

	
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		
		//1.从execution对象红获取流程实例id
		String processInstanceId = execution.getProcessInstanceId();
		
		//2.声明Member的认证状态变量
		Byte autostatus = 3;
		
		//3.*获取dataProviderRemoteService实例对象
		//①调用专门封装的静态方法获取IOC容器对象的引用
		WebApplicationContext iocContainerReference = IOCContainerReferenceProvider.getIOCContainerReference();
		
		//②从IOC容器中获取DatabaseProviderRemoteService的bean的实例
		DatabaseProviderRemoteService databaseProviderRemoteService = iocContainerReference.getBean(DatabaseProviderRemoteService.class);
		
		//4.调用databaseProviderRemoteService的远程方法修改t_ticket表和t_member表
		databaseProviderRemoteService.updateTicketAndMemberAfterCompleteProcessRemote(processInstanceId,autostatus);
		
		System.err.println("这是AllowListener---您的请求已通过");
	}

}
