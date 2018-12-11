package com.inno72.msg.center.util;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Md5Util {
	public static String getMD5String(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			return new BigInteger(1, md.digest()).toString(16);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
