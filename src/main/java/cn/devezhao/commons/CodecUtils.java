package cn.devezhao.commons;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Random;

import cn.devezhao.commons.encoder.Base64Encoder;
import cn.devezhao.commons.encoder.UrlBase64Encoder;

/**
 * 编码工具
 * 
 * @author Zhao Fangfang
 * @version $Id: Bean2Json.java 48 2015-08-18 02:57:54Z zhaofang123@gmail.com $
 */
public class CodecUtils {
	
	/** 0-9a-zA-Z */
	public static final String CODES64 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	private static final String ENCODING_UTF8 = "UTF-8";
	private static final Base64Encoder BASE64_ENCODER = new Base64Encoder();
	private static final UrlBase64Encoder URL_BASE64_ENCODER = new UrlBase64Encoder();
	
	/**
	 * BASE64 编码
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] base64Encode(byte[] data) {
		int length = (data.length + 2) / 3 * 4;
		ByteArrayOutputStream bOut = new ByteArrayOutputStream(length);
		try {
			BASE64_ENCODER.encode(data, 0, data.length, bOut);
		} catch (IOException e) {
			throw new RuntimeException("Exception encoding base64 string: " + e);
		}
		return bOut.toByteArray();
	}
	
	/**
	 * BASE64 解码
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] base64Decode(byte[] data) {
		int len = data.length / 4 * 3;
		ByteArrayOutputStream bOut = new ByteArrayOutputStream(len);
		try {
			BASE64_ENCODER.decode(data, 0, data.length, bOut);
		} catch (IOException e) {
			throw new RuntimeException("Exception decoding base64 string: " + e);
		}
		return bOut.toByteArray();
	}
	
	/**
	 * BASE64 URL 编码
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] base64UrlEncode(byte[] data) {
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		try {
			URL_BASE64_ENCODER.encode(data, 0, data.length, bOut);
		} catch (IOException e) {
			throw new RuntimeException("Exception encoding URL safe base64 string: " + e);
		}
		return bOut.toByteArray();
	}
	
	/**
	 * BASE64 UR L解码
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] base64UrlDecode(byte[] data) {
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		try {
			URL_BASE64_ENCODER.decode(data, 0, data.length, bOut);
		} catch (IOException e) {
			throw new RuntimeException("Exception decoding URL safe base64 string: " + e);
		}
		return bOut.toByteArray();
	}
	
	/**
	 * URL 编码
	 * 
	 * @param text
	 * @return
	 */
	public static String urlEncode(String text) {
		try {
			return URLEncoder.encode(text, ENCODING_UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Exception encoding URL string: " + e);
		}
	}
	
	/**
	 * URL 解码
	 * 
	 * @param text
	 * @return
	 */
	public static String urlDecode(String text) {
		try {
			return URLDecoder.decode(text, ENCODING_UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Exception decoding URL string: " + e);
		}
	}
	
	/**
	 * 10进制转换到任意进制
	 * 
	 * @param dec
	 * @param radix 2~62
	 * @return
	 */
	public static String dec2any(long dec, int radix) {
		if (radix < 2 || radix > 62) {
			radix = 2;
		}
		
		if (radix == 10) {
			return String.valueOf(dec);
		}

		char[] buf = new char[65];
		int pos = 64;
		boolean isNegative = (dec < 0);
        if (!isNegative) {
        	dec = -dec;
        }
        
		while (dec <= -radix) {
			buf[pos--] = CODES64.charAt((int) (-(dec % radix)));
			dec = dec / radix;
		}
		buf[pos] = CODES64.charAt((int) (-dec));
		return new String(buf, pos, (65 - pos));
	}
	
	/**
	 * 任意进制转换到10进制
	 * 
	 * @param number
	 * @param radix
	 * @return
	 */
	public static long any2dec(String number, int radix) {
		long dec = 0;
		long digitValue = 0;
		int len = number.length() - 1;
		for (int t = 0; t <= len; t++) {
			digitValue = CODES64.indexOf(number.charAt(t));
			dec = dec * radix + digitValue;
		}
		return dec;
	}
	
	/**
	 * 产生指定长度的随机码(码表: 0-9a-zA-Z)
	 * 
	 * @param length
	 * @return
	 */
	public static String randomCode(int length) {
		StringBuffer code = new StringBuffer();
		Random random = new SecureRandom();
		
		int codeLength = CODES64.length();
		for (int i = 0; i < length; i++) {
			code.append(CODES64.charAt(random.nextInt(codeLength)));
		}
		return code.toString();
	}
	
	private CodecUtils() {}
}
