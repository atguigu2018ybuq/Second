package com.hucheng.cfms.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Objects;
import com.hucheng.cfms.api.ActivitiProviderRemoteService;
import com.hucheng.cfms.api.DatabaseProviderRemoteService;
import com.hucheng.cfms.entity.CertDO;
import com.hucheng.cfms.entity.MemberCertDO;
import com.hucheng.cfms.entity.MemberDO;
import com.hucheng.cfms.entity.ParamCertListVO;
import com.hucheng.cfms.entity.ParamCertVO;
import com.hucheng.cfms.entity.ResultVO;
import com.hucheng.cfms.entity.TicketDO;
import com.hucheng.crowdfunding.constant.AttrName;
import com.hucheng.crowdfunding.constant.ErrorMessage;
import com.hucheng.crowdfunding.util.CrowdfundingStringUtils;

@Controller
public class ApplyHandler {

	@Autowired
	private ActivitiProviderRemoteService activitiProviderRemoteService;

	@Autowired
	private DatabaseProviderRemoteService databaseProviderRemoteService;

	@Autowired
	private StorageClient storageClient;
	
	
	
	/**
	 * 显示实名认证审核页面，并携带其需要的页面
	 */
	@RequestMapping("/consumer/management/approval/task/list")
	public String showApprovalTaskListRemtoe(Model model) {
		
		ResultVO<List<Map<String, Object>>> resultVO = activitiProviderRemoteService.getTaskListNeedApprovalRemote();
		
		List<Map<String, Object>> taskMapList = resultVO.getData();
		
		model.addAttribute("taskMapList", taskMapList);
		
		return "management/taskapproval";
	}
	/**
	 * 完成验证码的确认
	 */
	@RequestMapping("/member/process/code/check")
	public String processCodeCheck(HttpSession session, Model model, @RequestParam("code") String codeFromInput) {
		// 从Session域获取当前登录的用户验证用户是否登录
		MemberDO member = (MemberDO) session.getAttribute(AttrName.MEMBER_LOGIN_SESSION_ATTR_NAME);
		if (member == null) {
			model.addAttribute(AttrName.MESSAGE, ErrorMessage.ACCESS_FORBIDDEN);
			return "/member/login";
		}
		Integer memberId = member.getMemberId();
		ResultVO<TicketDO> resultVO = databaseProviderRemoteService.getTicketByMemberIdRemote(memberId);
		TicketDO ticketDO = resultVO.getData();
		// 获取数据库存储正确的验证码
		String codeFromDB = ticketDO.getAuthcode();
		if (Objects.equal(codeFromInput, codeFromDB)) {
			// 完成当前任务
			Map<String, Object> variables = new HashMap<>();
			String loginAcc = member.getLoginAcc();
			// 携带完成流程需要的变量
			variables.put("member", loginAcc);
			variables.put("flag", true);
			activitiProviderRemoteService.completeTaskByVariableRemote(variables);
			// 更新Member,设置状态为2
			Byte authStatus = 2;
			member.setAuthStatus(authStatus);
			databaseProviderRemoteService.UpdateMemberRemote(member);
			return "/member/main";
		} else {
			// 跳转回codecheck.page并回显示错误消息
			model.addAttribute(AttrName.MESSAGE, ErrorMessage.WRONG_CODE);
			return "apply/codecheck";
		}

	}

	/**
	 * 完成发送邮件的流程
	 */
	@RequestMapping("/member/process/email")
	public String processEmail(HttpSession session, Model model, @RequestParam("emailAddr") String emailAddr) {
		// 从Session域获取当前登录的用户验证用户是否登录
		MemberDO member = (MemberDO) session.getAttribute(AttrName.MEMBER_LOGIN_SESSION_ATTR_NAME);
		if (member == null) {
			model.addAttribute(AttrName.MESSAGE, ErrorMessage.ACCESS_FORBIDDEN);
			return "/member/login";
		}
		// 完成当前任务
		Map<String, Object> variables = new HashMap<>();
		String loginAcc = member.getLoginAcc();
		// 携带完成流程需要的变量
		// ①member用于作为委托人定位任务
		variables.put("member", loginAcc);
		// ②表示执行拍他网关中“下一步”分支
		variables.put("flag", true);
		// ③为邮件任务执行接收邮件的Email地址
		variables.put("memberEmailAddress", emailAddr);
		// ①存入随机验证码
		String code = CrowdfundingStringUtils.randomCode(4);
		variables.put("code", code);
		// 完成任务
		activitiProviderRemoteService.completeTaskByVariableRemote(variables);
		// 更新Member
		member.setEmailAddr(emailAddr);
		databaseProviderRemoteService.UpdateMemberRemote(member);
		// 更新t_ticket
		Integer memberId = member.getMemberId();
		String pStep = "codecheck";
		// 更改对应的pStep并保存验证码code
		databaseProviderRemoteService.updateTicketPstepAndCodeRemote(memberId, pStep, code);
		return "apply/" + pStep;
	}

	/**
	 * 上传资质文件到FastDFS文件管理服务器
	 */
	@RequestMapping("/member/process/upload/file")
	public String processFileUpload(HttpSession session, Model model, ParamCertListVO paramCertListVO)
			throws IOException, MyException {
		// 从Session域获取当前登录的用户验证用户是否登录
		MemberDO member = (MemberDO) session.getAttribute(AttrName.MEMBER_LOGIN_SESSION_ATTR_NAME);
		if (member == null) {
			model.addAttribute(AttrName.MESSAGE, ErrorMessage.ACCESS_FORBIDDEN);
			return "/member/login";
		}
		// 获取MemberId
		Integer memberId = member.getMemberId();

		List<ParamCertVO> paramList = paramCertListVO.getParamList();

		// 声明一个MemberCertDOList用来保存MemberCertDO
		List<MemberCertDO> MemberCertDOList = new ArrayList<>();

		for (ParamCertVO paramCertVO : paramList) {

			Integer certId = paramCertVO.getCertId();
			MultipartFile certFile = paramCertVO.getCertFile();

			// 获取该文件的字节数据
			byte[] file_buff = certFile.getBytes();
			String originalFilename = certFile.getOriginalFilename();

			// 获取文件后缀名
			String file_ext_name = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

			// 设置文件元数据为null
			NameValuePair[] meta_list = null;

			// 执行文件上传
			String[] upload_file = storageClient.upload_file(file_buff, file_ext_name, meta_list);

			// 获取在FastDFS中的组 名和文件名
			String groupName = upload_file[0];
			String remoteFileName = upload_file[1];

			// 图片在FastDFS可以访问的地址
			String iconPath = "http://192.168.163.211/" + groupName + "/" + remoteFileName;

			System.out.println(iconPath);

			// 封装MemberCert对象
			MemberCertDO memberCertDO = new MemberCertDO(null, memberId, certId, iconPath);
			// 将MemberCert存入List集合便于一次操作
			MemberCertDOList.add(memberCertDO);
		}
		// 一次性保存memberCertList
		databaseProviderRemoteService.saveMemberCertDOListRemote(MemberCertDOList);

		String pStep = "emailaddress";
		// 更新t_ticket表
		databaseProviderRemoteService.updateTicketPstepRemote(memberId, pStep);

		// 完成当前任务
		Map<String, Object> variables = new HashMap<>();
		String loginAcc = member.getLoginAcc();
		// 携带完成流程需要的变量
		variables.put("member", loginAcc);
		variables.put("flag", true);
		activitiProviderRemoteService.completeTaskByVariableRemote(variables);
		// 跳转到下一个流程页面
		return "apply/" + pStep;
	}

	/**
	 * 接收对来自传来的表单基本信息的处理
	 */
	@RequestMapping("/member/process/basic/info")
	public String dealWithProcessBasicInfo(HttpSession session, Model model, @RequestParam("realName") String realName,
			@RequestParam("cardNum") String cardNum, @RequestParam("phoneNum") String phoneNum) {
		// 从Session域获取当前登录的用户验证用户是否登录
		MemberDO member = (MemberDO) session.getAttribute(AttrName.MEMBER_LOGIN_SESSION_ATTR_NAME);
		if (member == null) {
			model.addAttribute(AttrName.MESSAGE, ErrorMessage.ACCESS_FORBIDDEN);
			return "/member/login";
		}
		// 1.完成“输入”基本信息任务
		// 从用户获取用户名作为委托人
		String loginAcc = member.getLoginAcc();
		Map<String, Object> variables = new HashMap<>();
		// 添加完成流程需要添加的两个参数：委托人和经过排它网关需要的flag值
		variables.put("member", loginAcc);
		variables.put("flag", true);
		// 完成任务
		activitiProviderRemoteService.completeTaskByVariableRemote(variables);
		// 2.更新Member
		// ①更新Session中的Member
		member.setRealName(realName);
		member.setCardNum(cardNum);
		member.setPhoneNum(phoneNum);
		// ②更新t_member表
		databaseProviderRemoteService.UpdateMemberRemote(member);
		// 3.更新Ticket
		Integer memberId = member.getMemberId();
		String pStep = "uploadfile";
		// 更新t_ticket需要参数memberId和pStep
		databaseProviderRemoteService.updateTicketPstepRemote(memberId, pStep);

		// ☆前往上传资质文件页面需要携带必要的数据！！
		Byte accType = member.getAccType();
		ResultVO<List<CertDO>> resultVO = databaseProviderRemoteService.getCertListByAccTypeRemote(accType);
		List<CertDO> certList = resultVO.getData();
		model.addAttribute("certList", certList);
		return "apply/" + pStep;
	}

	/**
	 * 完成用户类型的认证的操作
	 */
	@RequestMapping("/member/authentication/do/acct/type")
	public String memberAuthenticationDoAcctType(@RequestParam("accttype") Byte accttype, HttpSession session,
			Model model) {
		// 1.从Session域获取当前登录的用户
		MemberDO member = (MemberDO) session.getAttribute(AttrName.MEMBER_LOGIN_SESSION_ATTR_NAME);
		if (member == null) {
			model.addAttribute(AttrName.MESSAGE, ErrorMessage.ACCESS_FORBIDDEN);
			return "/member/login";
		}
		// 1.完成“账户类型选择”任务
		Map<String, Object> variables = new HashMap<>();
		String loginAcc = member.getLoginAcc();
		variables.put("member", loginAcc);
		activitiProviderRemoteService.completeTaskByVariableRemote(variables);
		// 2.修改Member和t_member的账户类型acc_type
		member.setAccType(accttype);
		databaseProviderRemoteService.UpdateMemberRemote(member);
		// 3.修改t_ticket的pStep
		String pStep = "basicinfo";
		Integer memberId = member.getMemberId();
		databaseProviderRemoteService.updateTicketPstepRemote(memberId, pStep);
		return "apply/" + pStep;
	}

	/**
	 * 继续申请流程
	 */
	@RequestMapping("/member/authentication/continue")
	public String continueProcessInstance(HttpSession session, Model model) {
		// 1.从Session域获取当前登录的用户
		MemberDO member = (MemberDO) session.getAttribute(AttrName.MEMBER_LOGIN_SESSION_ATTR_NAME);
		if (member == null) {
			model.addAttribute(AttrName.MESSAGE, ErrorMessage.ACCESS_FORBIDDEN);
			return "/member/login";
		}
		// 1.从当前用户对象中获取memberId
		Integer memberId = member.getMemberId();

		// 2.调用远程方法根据memberId查询t_ticket表中对应的pstep值
		ResultVO<String> resultVO = databaseProviderRemoteService.getPstepByMemberIdRemote(memberId);
		String pStep = resultVO.getData();

		// 特殊情况：如果pStep等于uploadfile，则需要接待必要的数据前往
		if ("uploadfile".equals(pStep)) {
			Byte accType = member.getAccType();
			ResultVO<List<CertDO>> certListResultVO = databaseProviderRemoteService.getCertListByAccTypeRemote(accType);
			List<CertDO> certList = certListResultVO.getData();
			model.addAttribute("certList", certList);
		}
		return "apply/" + pStep;
	}

	/**
	 * 初始化流程
	 */
	@RequestMapping("/member/authentication/start")
	public String startProcessInstance(HttpSession session, Model model) {

		// 1.从Session域获取当前登录的用户
		MemberDO member = (MemberDO) session.getAttribute(AttrName.MEMBER_LOGIN_SESSION_ATTR_NAME);
		if (member == null) {
			model.addAttribute(AttrName.MESSAGE, ErrorMessage.ACCESS_FORBIDDEN);
			return "/member/login";
		}
		// 2.调用activitiProviderRemoteService远程方法启动方法
		// ①获取Member的登录账号
		String loginAcc = member.getLoginAcc();
		// ②调用远程方法执行启动流程操作
		ResultVO<String> resultVO = activitiProviderRemoteService.startProcessInstanceRemote(loginAcc);
		// ③获取流程实例的id(!!!)
		String processInstanceID = resultVO.getData();

		// 3.初始化Ticket数据并调用databaseProviderRemoteService远程方法保存到数据库
		// ①初始化TicketDO对象
		TicketDO ticketDO = new TicketDO();
		Integer memberId = member.getMemberId();
		ticketDO.setMemberid(memberId);
		ticketDO.setPiid(processInstanceID);
		// Status初始化为0表示用户已经正在申请过程中了
		String status = "0";
		ticketDO.setStatus(status);
		// pstep初始化为accttype表示用户下一步需要进入账户类型选择页面
		String pstep = "accttype";
		ticketDO.setPstep(pstep);
		// ②调用远程方法保存TickerDO
		databaseProviderRemoteService.saveTicketRomote(ticketDO);

		// 4.将member的autoStatus修改为“正在申请”状态，也就是1
		Byte authStatus = 1;
		member.setAuthStatus(authStatus);
		// ①调用databaseProviderRemoteService远程方法修改数据库表中数据
		databaseProviderRemoteService.UpdateMemberRemote(member);
		// ②修改Session域中member对象中的authStatus属性
		return "apply/" + pstep;
	}
}
