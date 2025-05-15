package com.catering.config;

import javax.servlet.http.HttpServletRequest;

/**
 * Configuration class for retrieving the client IP address from the incoming HTTP request.
 * <p>
 * This class attempts to extract the real client IP address from various HTTP headers
 * that may be set by proxies or load balancers. If no valid IP is found in the headers, 
 * it falls back to the remote address from the request.
 * </p>
 * 
 * The class defines a static method {@link #getClientIpAddress(HttpServletRequest)} 
 * to fetch the client's IP address.
 * 
 * <p>
 * The method looks for the following HTTP headers in order:
 * <ul>
 * <li><b>X-Forwarded-For</b></li>
 * <li><b>Proxy-Client-IP</b></li>
 * <li><b>WL-Proxy-Client-IP</b></li>
 * <li><b>HTTP_X_FORWARDED_FOR</b></li>
 * <li><b>HTTP_X_FORWARDED</b></li>
 * <li><b>HTTP_X_CLUSTER_CLIENT_IP</b></li>
 * <li><b>HTTP_CLIENT_IP</b></li>
 * <li><b>HTTP_FORWARDED_FOR</b></li>
 * <li><b>HTTP_FORWARDED</b></li>
 * <li><b>HTTP_VIA</b></li>
 * <li><b>REMOTE_ADDR</b></li>
 * </ul>
 * </p>
 * 
 * If none of these headers contain a valid IP address, the method returns the IP 
 * address from the {@link HttpServletRequest#getRemoteAddr()} method.
 * 
 * @see HttpServletRequest
 */
public class IpAddressConfig {

	/**
	 * List of HTTP headers to try in order to obtain the real client IP address.
	 * These headers are typically set by proxies or load balancers.
	 */
	private static final String[] HEADERS_TO_TRY = { "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
			"HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",
			"HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR" };

	/**
	 * Retrieves the client IP address from the HTTP request.
	 * <p>
	 * This method checks various HTTP headers for the client's real IP address.
	 * If a valid IP is found in any of the headers, it is returned. If none of 
	 * the headers contain a valid IP, the method returns the remote address from 
	 * the request.
	 * </p>
	 * 
	 * @param request the HTTP request object
	 * @return the client IP address
	 */
	public static String getClientIpAddress(HttpServletRequest request) {
		// Iterate through headers to find a valid client IP
		for (String header : HEADERS_TO_TRY) {
			String ip = request.getHeader(header);
			if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
				return ip;
			}
		}

		return request.getRemoteAddr();
	}
}