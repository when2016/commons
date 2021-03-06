package cn.devezhao.commons;

import org.apache.commons.lang.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.zip.CRC32;

/**
 * 加密工具类
 * 
 * @author Zhao Fangfang
 * @version $Id: Bean2Json.java 48 2015-08-18 02:57:54Z zhaofang123@gmail.com $
 * @see CodecUtils
 */
public final class EncryptUtils {

	/**
	 * MD5加密
	 * 
	 * @param input
	 * @return
	 */
	private static byte[] toMD5(byte[] input) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		digest.update(input);
		return digest.digest();
	}
	
	/**
	 * MD5加密
	 * 
	 * @param input
	 * @return
	 */
	public static String toMD5Hex(byte[] input) {
		return toHexString(toMD5(input));
	}

	/**
	 * MD5加密
	 * 
	 * @param input
	 * @return
	 */
	public static String toMD5Hex(String input) {
		return toMD5Hex(input.getBytes(StandardCharsets.UTF_8));
	}
	
	/**
	 * SHA1加密
	 * 
	 * @param input
	 * @return
	 */
	private static byte[] toSHA(byte[] input, String algorithm) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		digest.update(input);
		return digest.digest();
	}
	
	/**
	 * SHA1加密
	 * 
	 * @param input
	 * @return
	 */
	public static String toSHA1Hex(byte[] input) {
		return toHexString(toSHA(input, "SHA1"));
	}

	/**
	 * SHA1加密
	 * 
	 * @param input
	 * @return
	 */
	public static String toSHA1Hex(String input) {
		return toSHA1Hex(input.getBytes(StandardCharsets.UTF_8));
	}
	
	/**
	 * SHA-256加密
	 * 
	 * @param input
	 * @return
	 */
	public static String toSHA256Hex(byte[] input) {
		return toHexString(toSHA(input, "SHA-256"));
	}

	/**
	 * SHA-256加密
	 * 
	 * @param input
	 * @return
	 */
	public static String toSHA256Hex(String input) {
		return toSHA256Hex(input.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * CRC32编码（6-8位）
	 * 
	 * @param input
	 * @return
	 */
	public static String toCRC32Hex(byte[] input) {
		CRC32 crc32 = new CRC32();
		crc32.update(input);
		return Long.toHexString( crc32.getValue() );
	}
	
	/**
	 * 返回8位CRC32编码，不够位数在尾部补0
	 * 
	 * @param input
	 * @return
	 */
	@Deprecated
	public static String toCRC32HexPad(byte[] input) {
		return toCRC32HexPadding(input);
	}
	
	/**
	 * 返回8位CRC32编码，不够位数在尾部补0
	 * 
	 * @param input
	 * @return
	 */
	public static String toCRC32HexPadding(byte[] input) {
		String crc32 = toCRC32Hex(input);
		if (crc32.length() == 8) return crc32;
		return StringUtils.rightPad(crc32, 8, '0');
	}

	/**
	 * @param data
	 * @return
	 */
	public static String toHexString(byte[] data) {
		StringBuilder sb = new StringBuilder();
		for (byte b : data) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
	
	/**
	 * AES加密
	 * 
	 * @param input
	 * @param passwd
	 * @return
	 */
	public static String aesEncrypt(String input, byte[] passwd) {
		try {
			SecretKeySpec key = buildAesSecretKey(passwd);
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);

			byte[] result = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
			return new String(CodecUtils.base64Encode(result));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * AES解密
	 * 
	 * @param input
	 * @param passwd
	 * @return
	 */
	public static String aesDecrypt(String input, byte[] passwd) {
		try {
			SecretKeySpec key = buildAesSecretKey(passwd);
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);

			byte[] result = cipher.doFinal(CodecUtils.base64Decode(input.getBytes(StandardCharsets.UTF_8)));
			return new String(result);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param passwd
	 * @return
	 */
	private static SecretKeySpec buildAesSecretKey(byte[] passwd) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(passwd));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			return new SecretKeySpec(enCodeFormat, "AES");

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private EncryptUtils() {}
}