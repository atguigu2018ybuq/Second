package com.hucheng.cfms.api;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hucheng.cfms.entity.ResultVO;

@FeignClient("CFMS-provider-activiti")
public interface ActivitiProviderRemoteService {

	/**
	 * 获取所有的流程定义对象(不能直接传输ProcessDefinition对象，需要将其转化成Map)
	 */
	@RequestMapping("/activiti/get/all/processes/remote")
	ResultVO<List<Map<String, String>>> getAllProcesses();

	/**
	 * 启动流程实例
	 */
	@RequestMapping("/activiti/start/process/instances/remote")
	public ResultVO<String> startProcessInstanceRemote(@RequestParam("loginAcc") String loginAcc);
	
	/**
	 * 完成任务并携带参数
	 */
	@RequestMapping("/complete/task/by/variable/remote")
	ResultVO<String> completeTaskByVariableRemote(@RequestBody Map<String, Object> variables);
	
	/**
	 * 显示实名认证审核页面，并携带其需要的页面
	 */
	@RequestMapping("/get/task/list/need/approval")
	public ResultVO<List<Map<String, Object>>> getTaskListNeedApprovalRemote();
	
	/**
	 * 完成管理审批
	 */
	@RequestMapping("/finish/process/remote")
	public ResultVO<String> finishProcessRemote(@RequestParam("flag") Boolean flag, @RequestParam("taskId") String taskId);
}
