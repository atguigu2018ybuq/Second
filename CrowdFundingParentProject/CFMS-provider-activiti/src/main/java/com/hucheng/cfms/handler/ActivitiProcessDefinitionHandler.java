package com.hucheng.cfms.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hucheng.cfms.api.DatabaseProviderRemoteService;
import com.hucheng.cfms.entity.MemberDO;
import com.hucheng.cfms.entity.ResultVO;

@RestController
public class ActivitiProcessDefinitionHandler {

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private DatabaseProviderRemoteService databaseProviderRemoteService;

	/**
	 * 完成管理审批
	 */
	@RequestMapping("/finish/process/remote")
	public ResultVO<String> finishProcessRemote(@RequestParam("flag") Boolean flag,
			@RequestParam("taskId") String taskId) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("flag", flag);
		taskService.complete(taskId, variables);
		return new ResultVO<String>(ResultVO.SUCCESS, ResultVO.NO_MSG, ResultVO.NO_DATA);
	};

	/**
	 * 显示实名认证审核页面，并携带其需要的页面
	 */
	@RequestMapping("/get/task/list/need/approval")
	public ResultVO<List<Map<String, Object>>> getTaskListNeedApprovalRemote() {
		// 1.声明流程定义key
		String processDefinitionKey = "CFMS";
		// 2.声明委托组
		String candidateGroup = "admin";
		// 3.查询符合委托组和流程定义key的任务列表
		List<Task> tastList = taskService.createTaskQuery().taskCandidateGroup(candidateGroup)
				.processDefinitionKey(processDefinitionKey).list();
		// 4.转换：Task->Map<String,Object>
		List<Map<String, Object>> taskMapList = new ArrayList<>();

		for (Task task : tastList) {
			// 获取当前任务的相关信息
			String taskId = task.getId();
			String taskName = task.getName();
			String processInstanceId = task.getProcessInstanceId();
			// 根据流程实例id在t_ticket结合t_member查询出对应的Member对象
			ResultVO<MemberDO> resultVO = databaseProviderRemoteService
					.getMemberByProcessInstanceIdRemote(processInstanceId);
			MemberDO member = resultVO.getData();
			Integer memberId = member.getMemberId();
			String loginAcc = member.getLoginAcc();

			// 将准备好的信息封装到taskMap
			Map<String, Object> taskMap = new HashMap<>();
			taskMap.put("taskId", taskId);
			taskMap.put("taskName", taskName);
			taskMap.put("processInstanceId", processInstanceId);
			taskMap.put("memberId", memberId);
			taskMap.put("loginAcc", loginAcc);
			taskMapList.add(taskMap);
		}
		return new ResultVO<List<Map<String, Object>>>(ResultVO.SUCCESS, ResultVO.NO_MSG, taskMapList);
	}

	/**
	 * 完成任务并携带参数
	 */
	@RequestMapping("/complete/task/by/variable/remote")
	ResultVO<String> completeTaskByVariableRemote(@RequestBody Map<String, Object> variables) {
		String loginAcc = (String) variables.get("member");
		TaskQuery taskQuery = taskService.createTaskQuery();

		// 将loginAcc指定为任务委托人
		taskQuery = taskQuery.taskAssignee(loginAcc);

		// 查询任务
		List<Task> taksList = taskQuery.list();
		for (Task task : taksList) {
			// 获取当前任务的id
			String taskId = task.getId();
			// 完成当前任务
			taskService.complete(taskId, variables);
		}
		return new ResultVO<String>(ResultVO.SUCCESS, ResultVO.NO_MSG, ResultVO.NO_DATA);
	}

	/**
	 * 启动流程实例
	 */
	@RequestMapping("/activiti/start/process/instances/remote")
	public ResultVO<String> startProcessInstanceRemote(@RequestParam("loginAcc") String loginAcc) {
		// 查询流程定义的依据：key
		String processDefinitionKey = "CFMS";

		// 1.查询流程定义
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey(processDefinitionKey).latestVersion().singleResult();

		// 2.获取流程实例的id
		String processDefinitionId = processDefinition.getId();

		// 3.启动流程
		// ①封装流程变量
		HashMap<String, Object> variables = new HashMap<>();
		variables.put("member", loginAcc);
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId, variables);

		// 4.获取流程实例id
		String processInstanceId = processInstance.getId();

		return new ResultVO<String>(ResultVO.SUCCESS, ResultVO.NO_MSG, processInstanceId);

	}

	/**
	 * 提供从数据库中获取所有processeDefinition的远程方法
	 */
	@RequestMapping("/activiti/get/all/processes/remote")
	public ResultVO<List<Map<String, String>>> getAllProcesses() {

		// 1.查询全部的ProcessDefinition的list
		List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();

		// 2.因为直接传输ProcessDefinition对象会抛异常,所以我们将 需要该对象的信息封装到Map中，然后用Map传输
		List<Map<String, String>> list = new ArrayList<>();

		// 3.遍历processDefinitionList
		for (ProcessDefinition processDefinition : processDefinitionList) {

			// 4.从ProcessDefinition对象中获取需要的数据
			String processDefinitionId = processDefinition.getId();
			String processDefinitionName = processDefinition.getName();
			String processDefinitionKey = processDefinition.getKey();
			int processDefinitionVersion = processDefinition.getVersion();
			// 5.封装到Map中，然后添加到list中
			Map<String, String> map = new HashMap<>();
			map.put("id", processDefinitionId);
			map.put("name", processDefinitionName);
			map.put("key", processDefinitionKey);
			map.put("version", processDefinitionVersion + "");
			list.add(map);
		}

		return new ResultVO<List<Map<String, String>>>(ResultVO.SUCCESS, ResultVO.NO_MSG, list);
	}

	/**
	 * 将传入进来的.bpmn文件部署到数据库中
	 */
	@RequestMapping("/activiti/process/deploy")
	public String processDeploy(@RequestParam("processDefinitionFile") MultipartFile processDefinitionFile)
			throws IOException {
		// 1.获取用来部署流程文件的输入流对象
		InputStream inputStream = processDefinitionFile.getInputStream();
		// 2.获取原始文件名作为resoureName
		String resourceName = processDefinitionFile.getOriginalFilename();
		// 3.执行部署
		repositoryService.createDeployment().addInputStream(resourceName, inputStream).deploy();

		return "SUCCESS";
	}

	/**
	 * 获取流程图
	 */
	@RequestMapping("/activiti/get/process/picture/{key}/{version}")
	public byte[] getPicture(@PathVariable("key") String key, @PathVariable("version") Integer version) {
		// 1.查询对象的流程定义的对象
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key)
				.processDefinitionVersion(version).singleResult();
		// 2.从流程定义对象中获取相关数据
		// 部署id
		String deploymentId = processDefinition.getDeploymentId();
		// 图片的资源名称
		String diagramResourceName = processDefinition.getDiagramResourceName();
		// 3.查询对象图片的输入流
		InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, diagramResourceName);
		// 4.创建字节数组的输出流
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		// 5.创建存储临时数据的字节数组
		byte[] buffer = new byte[100];
		int rc = 0;
		// 6.从输入流读取数据，写入输出流
		try {
			while ((rc = inputStream.read(buffer, 0, 100)) > 0) {
				outputStream.write(buffer, 0, rc);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 7.从ByteArrayOutputStream中获取存储全部数据的字节数组
		return outputStream.toByteArray();
	}
}
