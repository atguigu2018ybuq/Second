package com.hucheng.cfms.handler;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.hucheng.cfms.api.ActivitiProviderRemoteService;
import com.hucheng.cfms.entity.ResultVO;

@Controller
public class ProcessDefinitionHandler {

	@Autowired
	private ActivitiProviderRemoteService activitiProviderRemoteServicel;

	// Feign不能传输二进制数据，我们只能重新用restTemplate
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 将processDefinitionFile对象中的二进制文件数据 上传到Provider
	 * 
	 * @throws IOException
	 */
	@RequestMapping("/consumer/manager/process/upload")
	public String doUpload(@RequestParam("processDefinitionFile") MultipartFile processDefinitionFile)
			throws IOException {
		boolean empty = processDefinitionFile.isEmpty();

		// ※检查是否上传的了文件
		if (empty) {
			throw new RuntimeException("请上传有效文件！");
		}
		// 总目标：将processDefinitionFile对象中的二进制文件数据上传到Provider
		// 1.获取原始文件名
		String originalFilename = processDefinitionFile.getOriginalFilename();

		// 2.根据元素文件名截取文件扩展名
		String extensiveName = originalFilename.substring(originalFilename.lastIndexOf("."));

		// ※检查文件扩展名
		if (!".bpmn".equals(extensiveName)) {
			throw new RuntimeException("请上传流程定义文件！");
		}

		// 3.拼装临时文件名
		String templeFileName = UUID.randomUUID().toString();

		// 4.创建File对象
		File templeFile = File.createTempFile(templeFileName, extensiveName);

		// 5.转存
		processDefinitionFile.transferTo(templeFile);

		// 6.封装到FileSystemResource对象中
		FileSystemResource fileSystemResource = new FileSystemResource(templeFile);

		// 7.创建LinkedMultiValueMap对象用来封装请求参数
		LinkedMultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();

		paramMap.add("processDefinitionFile", fileSystemResource);

		// 8.调用RestTemplate对象的方法发送请求
		// ①声明URL地址字符串
		String url = "http://CFMS-provider-activiti/activiti/process/deploy";

		// ②声明返回值类型
		Class<String> clazz = String.class;

		// ③发送请求
		String response = restTemplate.postForObject(url, paramMap, clazz);

		System.out.println("response=" + response);

		return "redirect:/consumer/management/process/definition/list";
		/*
		 * // 1.获取原始文件名 String originalFilename =
		 * processDefinitionFile.getOriginalFilename(); // 2.根据元素文件名截取文件扩展名 String
		 * extensiveName =
		 * originalFilename.substring(originalFilename.lastIndexOf(".")); // 3.拼装临时文件名
		 * String templateFileName = UUID.randomUUID().toString(); // 4.创建File对象 File
		 * tempFile = File.createTempFile(templateFileName, extensiveName); // 5.转存
		 * processDefinitionFile.transferTo(tempFile); // 6.封装到FileSystemResource对象中
		 * FileSystemResource fileSystemResource = new FileSystemResource(tempFile); //
		 * 7.创建LinkedMultiValueMap对象来封装请求参数 LinkedMultiValueMap<String, Object> paramMap
		 * = new LinkedMultiValueMap<>(); //
		 * 存入参数（processDefinitionFile是将来Provider接收文件时请求参数名称）
		 * paramMap.add("processDefinitionFile", fileSystemResource); //
		 * 8.调用RestTemplate对象的方法发送请求 // ①声明URL地址字符串 String url =
		 * "http://CFMS-provider-activiti/activiti/process/deploy"; // ②发送请求，后面为返回值的类型
		 * String response = restTemplate.postForObject(url, paramMap, String.class);
		 * System.err.println(response); // 9.重定本页 return
		 * "redirect:/consumer/management/process/definition/list";
		 */
	}

	/**
	 * 前往业务管理下的流程管理页面，并携带ProcessDefinition数据
	 */
	@RequestMapping("/consumer/management/process/definition/list")
	public String showProcessDefinitionList(Model model) {
		ResultVO<List<Map<String, String>>> resultVO = activitiProviderRemoteServicel.getAllProcesses();
		List<Map<String, String>> processDefinitionMapList = resultVO.getData();
		model.addAttribute("processDefinitionMapList", processDefinitionMapList);
		return "management/process";
	}

	/**
	 * 
	 */
	@RequestMapping("/manager/consumer/eye/picture/{key}/{version}")
	public void achieveEyePicture(@PathVariable("key") String key, @PathVariable("version") Integer version,
			HttpServletResponse response) throws IOException {
		// 1.声明字符串存储Provider远程方法的URL地址
		String url = "http://CFMS-provider-activiti/activiti/get/process/picture/" + key + "/" + version;
		// 2.为了封装消息头信息创建HttpHeaders方法
		HttpHeaders httpHeaders = new HttpHeaders();
		// 3.设置内容类型，表示我们想要接收的是一个PNG图片
		httpHeaders.setContentType(MediaType.IMAGE_PNG);
		// 4.创建HttpEntity对象封装headers
		HttpEntity<byte[]> requestEntity = new HttpEntity<>(httpHeaders);
		// 5.发送请求
		ResponseEntity<byte[]> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				byte[].class);
		// 6.获取响应体数据
		byte[] body = responseEntity.getBody();
		// 7.通过response对象获取输出流
		// 给浏览器返回字节数据的输出流
		ServletOutputStream outputStream = response.getOutputStream();
		//8.使用字节数组输入流封装响应体数据
		ByteArrayInputStream inputStream = new ByteArrayInputStream(body);
		//9.从inputStream读取，写去outputStream
		//①声明变量用于临时存储字节数据
		int byteData = 0;
		//②在循环中心功能一边读一边写
		while ((byteData=inputStream.read())!=-1 ) {
			outputStream.write(byteData);
		}
		
	}
}
