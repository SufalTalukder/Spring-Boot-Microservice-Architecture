package com.sufaltalukder.Utils;

import jakarta.servlet.http.HttpServletRequest;

public class AuthInfoUtil {

	private static boolean isBlank(String s) {
		return s == null || s.isBlank();
	}

	public static String getClientIp(HttpServletRequest request) {
		String xff = request.getHeader("X-Forwarded-For");
		if (xff != null && !xff.isEmpty()) {
			return xff.split(",")[0];
		}
		return request.getRemoteAddr();
	}

	public static String getBrowser(String ua) {
		if (isBlank(ua))
			return "UNKNOWN";
		ua = ua.toLowerCase();
		if (ua.contains("edg"))
			return "Edge";
		if (ua.contains("chrome"))
			return "Chrome";
		if (ua.contains("firefox"))
			return "Firefox";
		if (ua.contains("safari"))
			return "Safari";
		return "OTHER";
	}

	public static String getOS(String ua) {
		if (isBlank(ua))
			return "UNKNOWN";
		ua = ua.toLowerCase();
		if (ua.contains("windows"))
			return "Windows";
		if (ua.contains("mac"))
			return "MacOS";
		if (ua.contains("android"))
			return "Android";
		if (ua.contains("iphone") || ua.contains("ipad"))
			return "iOS";
		if (ua.contains("linux"))
			return "Linux";
		return "OTHER";
	}

	public static String getDeviceType(String ua) {
		if (isBlank(ua))
			return "UNKNOWN";
		ua = ua.toLowerCase();
		if (ua.contains("mobile"))
			return "MOBILE";
		if (ua.contains("tablet"))
			return "TABLET";
		return "DESKTOP";
	}

	public static boolean possibleIncognito(String userAgent) {
		return false;
	}
}
