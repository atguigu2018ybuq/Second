package com.hucheng.cfms.handler;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hucheng.cfms.api.DatabaseProviderRemoteService;
import com.hucheng.cfms.entity.MemberDO;
import com.hucheng.cfms.entity.ResultVO;
import com.hucheng.cfms.entity.UserDO;
import com.hucheng.crowdfunding.constant.AttrName;
import com.hucheng.crowdfunding.constant.ErrorMessage;
import com.hucheng.crowdfunding.util.CrowdfundingStringUtils;

@Controller
public class MemberHandler {

	@Autowired
	private DatabaseProviderRemoteService databaseProviderRemoteService;

	/**
	 * 实现会员登录
	 */
	@ResponseBody
	@RequestMapping("/member/do/login")
	public ResultVO<String> doLogin(MemberDO MemberDOFromForm, HttpSession session) {
		// 1.通过传来的MemberDO的用户名调用远程方法查询对应的User对象
		MemberDO MemberDOFromDB = databaseProviderRemoteService.getUserByLoginacct(MemberDOFromForm);
		// 2.创建需要的返回ResultVO对象
		ResultVO<String> resultVO = new ResultVO<>();
		// 3.这个密码是明文（用户输入）
		String userpswdFromForm = MemberDOFromForm.getLoginPwd();
		// 4.检查MemberDOFromDB是否为空
		if (MemberDOFromDB == null) {
			// 4.如果为null，判定登录失败，返回ResultVO
			resultVO.setResult(ResultVO.FAILED);
			resultVO.setData(ResultVO.NO_DATA);
			resultVO.setMessage(ErrorMessage.ACTT_NOT_EXISTS);
			return resultVO;
		}
		// 6.从数据库查询出来的MemberDOFromDB获取密码
		// 注意：这个密码是密文
		String userpswdFromDB = MemberDOFromDB.getLoginPwd();
		// 7.将传来的明文密码加密
		String userpswdFromFormAfterMD5 = CrowdfundingStringUtils.md5(userpswdFromForm);
		if (!userpswdFromDB.equals(userpswdFromFormAfterMD5)) {
			// 如果不一致，这判定登录失败，原因密码不正确
			resultVO.setResult(ResultVO.FAILED);
			resultVO.setData(ResultVO.NO_DATA);
			resultVO.setMessage(ErrorMessage.NOT_MATCH);
			return resultVO;
		}
		// 7.执行到这里，可以判定成功，将userDaoFromDB存入到Session域中
		session.setAttribute(AttrName.MEMBER_LOGIN_SESSION_ATTR_NAME, MemberDOFromDB);
		resultVO.setResult(ResultVO.SUCCESS);
		resultVO.setData(ResultVO.NO_DATA);
		resultVO.setMessage(ResultVO.NO_MSG);
		return resultVO;
	}

	/**
	 * 实现用户登录
	 */
	@ResponseBody
	@RequestMapping("/user/do/login")
	public ResultVO<String> doLogin(UserDO userDOFromForm, HttpSession session) {
		// 1.通过传来的userDO的用户名调用远程方法查询对应的User对象
		UserDO userDOFromDB = databaseProviderRemoteService.getUserByLoginacct(userDOFromForm);
		// 2.创建需要的返回ResultVO对象
		ResultVO<String> resultVO = new ResultVO<>();
		// 3.这个密码是明文（用户输入）
		String userpswdFromForm = userDOFromForm.getUserpswd();
		// 4.检查userDOFromDB是否为空
		if (userDOFromDB == null) {
			// 4.如果为null，判定登录失败，返回ResultVO
			resultVO.setResult(ResultVO.FAILED);
			resultVO.setData(ResultVO.NO_DATA);
			resultVO.setMessage(ErrorMessage.ACTT_NOT_EXISTS);
			return resultVO;
		}
		// 6.从数据库查询出来的userDOFromDB获取密码
		// 注意：这个密码是密文
		String userpswdFromDB = userDOFromDB.getUserpswd();
		// 7.将传来的明文密码加密
		String userpswdFromFormAfterMD5 = CrowdfundingStringUtils.md5(userpswdFromForm);
		if (!userpswdFromDB.equals(userpswdFromFormAfterMD5)) {
			// 如果不一致，这判定登录失败，原因密码不正确
			resultVO.setResult(ResultVO.FAILED);
			resultVO.setData(ResultVO.NO_DATA);
			resultVO.setMessage(ErrorMessage.NOT_MATCH);
			return resultVO;
		}
		// 7.执行到这里，可以判定成功，将userDaoFromDB存入到Session域中
		session.setAttribute(AttrName.USER_LOGIN_SESSION_ATTR_NAME, userDOFromDB);
		resultVO.setResult(ResultVO.SUCCESS);
		resultVO.setData(ResultVO.NO_DATA);
		resultVO.setMessage(ResultVO.NO_MSG);
		return resultVO;
	}

	

	/**
	 * 注册功能的实现
	 * 
	 * @param memberDO
	 *            member实体类对象
	 * @param model
	 *            用户保存message信息的域
	 */
	@RequestMapping("/member/do/register")
	public String doRegister(MemberDO memberDO, Model model) {

		// 1.获取loginacct值
		String loginAcc = memberDO.getLoginAcc();

		// 2.调用远程方法查询loginAcc的数量
		ResultVO<Integer> resultVO = databaseProviderRemoteService.getLoginAcctCountRemote(loginAcc);

		// 3.获取结果
		String result = resultVO.getResult();
		if (ResultVO.SUCCESS.equals(result)) {

			Integer count = resultVO.getData();

			if (count == 0) {
				// 可以注册，保存Member
				ResultVO<String> saveMemverResultVO = databaseProviderRemoteService.saveMemverRemote(memberDO);

				// 保存的时候时候的状况
				String saveResult = saveMemverResultVO.getResult();

				if (ResultVO.SUCCESS.equals(saveResult)) {
					// 保存成功，前往到登录页面
					return "info/login";
				} else {
					// 保存失败，返回到登录页面，并回显错误信息
					String message = saveMemverResultVO.getMessage();
					model.addAttribute(AttrName.MESSAGE, message);
					return "info/register";
				}
			}
			// 已存在该用户名
			model.addAttribute(AttrName.MESSAGE, ErrorMessage.LOGIN_ACCT_ALREADY_EXISTS);
			return "info/register";
		}
		// 注册失败：发生异常 回显异常信息；返回到注册页面
		model.addAttribute(AttrName.MESSAGE, resultVO.getMessage());
		return "info/register";
	}
	
}
