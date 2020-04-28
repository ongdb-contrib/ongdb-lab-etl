package data.lab.ongdb.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 计算MD5
 * @author Chenjl
 *
 */

public class MD5Digest {

	private static MessageDigest digest = null;
	
	
	static {
		try {
				digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 计算md5
	 * @param message
	 * @return
	 */
	public static String MD5(String message){
		try {
			byte[] bytes = digest.digest(message.getBytes());
			StringBuffer output = new StringBuffer(bytes.length);
			for (int i = 0; i < bytes.length; i++) {
				output.append(String.format("%02x", bytes[i]));
			}
			digest.reset();
			return output.toString();
		} catch (Exception e) {
			System.err.println("inputString:" + message);
			e.printStackTrace();
		}
		return null;
	}
	
	
	public MD5Digest() {
		
	}

	public static void main(String[] args) {
		
//		String message = "如今的世界？正义与公理甚至人类良知与道德底线，在强权和霸道面前，总是那么苍白和不堪一击。现如今的联合？";
		String message = "http://news.qq.com/a/20131026/000170.htm";
		String md5 = MD5Digest.MD5(message);
		System.out.println(md5);
	}
}

