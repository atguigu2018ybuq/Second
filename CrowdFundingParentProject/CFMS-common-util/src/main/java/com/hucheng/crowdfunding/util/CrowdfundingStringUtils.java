package com.hucheng.crowdfunding.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.hucheng.crowdfunding.constant.ErrorMessage;

public class CrowdfundingStringUtils {
	/**
	 * 生成一个随机验证码
	 * 
	 * @param length:验证码的长度
	 */
	public static String randomCode(int length) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			double random = Math.random();
			int code = (int)(random*10);
			builder.append(code);
		}
		return builder.toString();
	}

	/**
	 * 验证一个字符串是否有效：非空且字符串长度不为0
	 */
	public static boolean stringCheck(String source) {
		return source != null && source.length() != 0;
	}

	/**
	 * 执行MD5加密的工具方法
	 */
	public static String md5(String source) {
		// 1.对字符串进行校验
		boolean stringCheck = CrowdfundingStringUtils.stringCheck(source);

		// 2.如果字符串校验失败，则抛出一个异常
		if (!stringCheck) {
			throw new RuntimeException(ErrorMessage.MD5_SOURCE_MISSING);
		}

		// 3.将源字符串转换成字节数组
		byte[] inputBytes = source.getBytes();

		// 4.获取MessageDigest(抽象类)实例
		String algorithm = "MD5";
		// 5.声明变量存储加密结果
		byte[] outputBytes = null;

		try {

			// 6.获取MessageDigest实例
			MessageDigest digest = MessageDigest.getInstance(algorithm);

			// 7.执行加密
			outputBytes = digest.digest(inputBytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// 8.把加密结果字节数组转化成字符串
		// ①声明StringBuilder
		StringBuilder builder = new StringBuilder();
		// ②声明字符数组
		char[] characters = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
				'F' };
		// ③遍历outputBytes
		for (int i = 0; i < outputBytes.length; i++) {
			byte b = outputBytes[i];
			// b为1字节，8位，
			int lowValue = b & 15;
			int highValue = (b >> 4) & 15;
			char lowChar = characters[lowValue];
			char highChar = characters[highValue];
			builder.append(lowChar).append(highChar);
		}
		return builder.toString();
	}
}
