package com.hucheng.cfms.api;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hucheng.cfms.entity.ApprovalDetailVO;
import com.hucheng.cfms.entity.CertDO;
import com.hucheng.cfms.entity.MemberCertDO;
import com.hucheng.cfms.entity.MemberDO;
import com.hucheng.cfms.entity.ResultVO;
import com.hucheng.cfms.entity.TicketDO;
import com.hucheng.cfms.entity.UserDO;

/**
 * 这个注释用于将 CFMS-provider-database微服务的功能暴露出来 FeignClient注解指定要暴露功能的微服务的名称
 * 微服务的名称是在对应工程的application.yml使用spring.application.name定义的 也可以在Eureka注册中心可以看到
 * 
 * @author HuCheng
 *
 */
@FeignClient("CFMS-provider-database")
public interface DatabaseProviderRemoteService {
	/**
	 * 通过acctype账户类型来获取对应的需要提供的证明资质文件名
	 */
	@RequestMapping("/get/cert/list/by/acctype/remote")
	public ResultVO<List<CertDO>> getCertListByAccTypeRemote(@RequestParam("accType") Byte accType);

	/**
	 * 保存流程审批表单ticketDO对象到数据库中
	 */
	@RequestMapping("save/ticket/remote")
	public ResultVO<String> saveTicketRomote(@RequestBody TicketDO ticketDO);

	/**
	 * 提供远程更新MemberDO(实际上是为了更新Member对象的auto_status)
	 */
	@RequestMapping("/update/member/remote")
	public ResultVO<String> UpdateMemberRemote(@RequestBody MemberDO memberDO);

	/**
	 * 提供通过会员对应的会员名查询出在数据库中的会员对象
	 */
	@RequestMapping("/get/member/by/loginacc")
	public MemberDO getUserByLoginacct(@RequestBody MemberDO memberDOFromForm);

	/**
	 * 对外提供根据loginAcct查询结果数量的方法
	 * 
	 * @param loginAcct
	 * @return 结果实体类对象
	 */
	@RequestMapping("/get/login/acct/count")
	public ResultVO<Integer> getLoginAcctCountRemote(@RequestParam("loginAcct") String loginAcct);

	/**
	 * 对外提供存入用户的方法
	 * 
	 * @param memberDO
	 *            Member对象
	 * @return 结果实体类对象
	 */
	@RequestMapping("/save/member/remote")
	public ResultVO<String> saveMemverRemote(@RequestBody MemberDO memberDO);

	/**
	 * 提供通过用户名查询出在数据库中的用户对象
	 */
	@RequestMapping("/get/user/by/loginacct")
	public UserDO getUserByLoginacct(@RequestBody UserDO userDOFromForm);

	/**
	 * 调用远程方法根据memberId查询t_ticket表中对应的pstep值
	 */
	@RequestMapping("/get/pstep/by/memberId/remote")
	public ResultVO<String> getPstepByMemberIdRemote(@RequestParam("memberId") Integer memberId);

	/**
	 * 修改t_ticket表的pstep下一个任务
	 */
	@RequestMapping("/update/ticket/pstep/remote")
	public ResultVO<String> updateTicketPstepRemote(@RequestParam("memberId") Integer memberId,
			@RequestParam("pStep") String pStep);

	/**
	 * 保存 memberCertDOList中的memberCertDO到t_member_cert表中
	 */
	@RequestMapping("/save/membercertdo/list/remote")
	public ResultVO<String> saveMemberCertDOListRemote(@RequestBody List<MemberCertDO> memberCertDOList);

	/**
	 * 更改t_ticket表中的pStep并保存验证码code
	 */
	@RequestMapping("/update/ticket/pstep/and/code/remote")
	public ResultVO<String> updateTicketPstepAndCodeRemote(@RequestParam("memberId") Integer memberId,
			@RequestParam("pStep") String pStep, @RequestParam("code") String code);

	/**
	 * 通过memberId查询出对应的ticketDO对象
	 */
	@RequestMapping("/get/ticket/by/memberid/remote")
	public ResultVO<TicketDO> getTicketByMemberIdRemote(@RequestParam("memberId") Integer memberId);

	/**
	 * 通过processInstanceId查询出对应的MemberDO对象
	 */
	@RequestMapping("/get/member/by/processinstanceid/remote")
	public ResultVO<MemberDO> getMemberByProcessInstanceIdRemote(
			@RequestParam("processInstanceId") String processInstanceId);

	/**
	 * 点击查看详细信息可以查看会员申请流程的数据和图
	 */
	@RequestMapping("/get/process/approval/detail/remote")
	public ResultVO<ApprovalDetailVO> getProcessApprovalDetailRemote(@RequestParam("memberId") String memberId);

	/**
	 * 通过流程实例的processInstanceId查出对应的Member对象并修改Member对象的autoStatus
	 */
	@RequestMapping("/update/ticket/and/member/after/complete/process")
	public ResultVO<String> updateTicketAndMemberAfterCompleteProcessRemote(
			@RequestParam("processInstanceId") String processInstanceId, @RequestParam("autostatus") Byte autostatus);

}
