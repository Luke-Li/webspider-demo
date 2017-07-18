package com.spiderman.util.url;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.cookie.Cookie;

import com.spiderman.util.CookieUtil;

import net.kernal.spiderman.worker.download.Downloader;

public class URLUtil {
	private static List<Downloader.Cookie> cookieList = new ArrayList<>();
	private static boolean isCookie = false;

	public static HashMap<String, String> ParseQueryString(String query) {
		return ParseQueryString(query, "utf-8");
	}

	public static HashMap<String, String> ParseQueryString(String query, String encoding) {
		if (query == null) {
			throw new NullPointerException("query");
		}
		if (encoding == null) {
			throw new NullPointerException("encoding");
		}
		if ((query.length() > 0) && (query.charAt(0) == '?')) {
			query = query.substring(1);
		}
		return FillFromString(query, false, encoding);
	}

	private static HashMap<String, String> FillFromString(String s, Boolean urlencoded, String encoding) {
		HashMap<String, String> httpValueCollectionMap = new HashMap<String, String>();
		try {
			int num = (s != null) ? s.length() : 0;
			for (int i = 0; i < num; i++) {
				int startIndex = i;
				int num4 = -1;
				while (i < num) {
					char ch = s.charAt(i);
					if (ch == '=') {
						if (num4 < 0) {
							num4 = i;
						}
					} else if (ch == '&') {
						break;
					}
					i++;
				}
				String str = null;
				String str2 = null;
				if (num4 >= 0) {
					str = s.substring(startIndex, num4);
					str2 = s.substring(num4 + 1, i);
				} else {
					str2 = s.substring(startIndex, i);
				}
				if (urlencoded) {
					httpValueCollectionMap.put(URLDecoder.decode(str, encoding), URLDecoder.decode(str2, encoding));
				} else {
					httpValueCollectionMap.put(str, str2);
				}
				if ((i == (num - 1)) && (s.charAt(i) == '&')) {
					httpValueCollectionMap.put(null, "");
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return httpValueCollectionMap;
	}

	public static void fillRequestHeader(Downloader.Request request, String referer) {
		if (request != null) {
			request.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			request.addHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.59 Safari/537.36");
			request.addHeader("X-Requested-With", "XMLHttpRequest");
			request.addHeader("Referer", referer);
			request.addHeader("Accept-Encoding", "gzip,deflate");
			request.addHeader("Accept-Language", "en-us,en");

			if (isCookie == true) {
				// addCookies(request);
			} else {
				isCookie = true;
				request.addCookie(".ASPXAUTH",CookieUtil.getProperty(".ASPXAUTH"));
				request.addCookie("ct",CookieUtil.getProperty("ct"));
				request.addCookie("ut",CookieUtil.getProperty("ut"));
				request.addCookie("pi",CookieUtil.getProperty("pi"));
				
			}
		}

	}

	public static void setCookies(List<Downloader.Cookie> cookies) {
		cookieList = cookies;
	}

	public static void insertCookies(List<Cookie> cookies) {
		if (cookies == null || cookies.isEmpty()) {
			return;
		}
		if (cookieList != null) {
			cookieList.clear();
		}
		for (int i = 0; i < cookies.size(); i++) {
			cookieList.add(new Downloader.Cookie(cookies.get(i).getName(), cookies.get(i).getValue()));
		}
	}

	public static void addCookies(Downloader.Request request) {
		if (cookieList != null && !cookieList.isEmpty()) {
			for (int i = 0; i < cookieList.size(); i++) {
				Downloader.Cookie tmp = cookieList.get(i);
				request.addCookie(tmp.getName(), tmp.getValue());
			}
		}
	}
}
