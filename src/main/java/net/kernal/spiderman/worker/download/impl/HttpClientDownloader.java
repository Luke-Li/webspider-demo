package net.kernal.spiderman.worker.download.impl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.log4j.Logger;

import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;
import com.spiderman.util.HttpclientUtils;
import net.kernal.spiderman.kit.K;
import net.kernal.spiderman.kit.Properties;
import net.kernal.spiderman.worker.download.Downloader;

/**
 * 默认下载器，基于HttpClient实现
 * @author 赖伟威 l.weiwei@163.com 2015-12-10
 *
 */
public class HttpClientDownloader extends Downloader {

	private RequestConfig defaultRequestConfig;
	private CloseableHttpClient httpClient;
	private CookieStore cookieStore;  
	private Map<String, String> headers;
	private Escaper urlEscaper;
	
	Logger logger = Logger.getLogger(getClass());
	
	public HttpClientDownloader() {
		this(new Properties());
	}
	
	public HttpClientDownloader(Properties props) {
		RequestConfig.Builder builder = RequestConfig.custom()
	            .setCookieSpec(CookieSpecs.NETSCAPE)
	            .setExpectContinueEnabled(true)
	            .setRedirectsEnabled(props.getBoolean("downloader.redirectsEnabled", true))
	            .setCircularRedirectsAllowed(props.getBoolean("downloader.circularRedirectsAllowed", true))
	            // 设置从连接池获取连接的超时时间
	            .setConnectionRequestTimeout(props.getInt("downloader.connectionRequestTimeout", 30000))
	            // 设置连接远端服务器的超时时间
	            .setConnectTimeout(props.getInt("downloader.connectTimeout",120000))
	            // 设置从远端服务器上传输数据回来的超时时间
	            .setSocketTimeout(props.getInt("downloader.socketTimeout", 60000))
	            .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
	            .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC));
		
		String proxy = props.getString("downloader.proxy");
		if (K.isNotBlank(proxy)) {
			builder.setProxy(HttpHost.create(proxy));
		}
		this.cookieStore = new BasicCookieStore();
		this.headers = new HashMap<String, String>();
	    this.defaultRequestConfig = builder.build();
	    HttpClientBuilder hcb = HttpClients.custom();
		this.httpClient = hcb
				.setUserAgent(props.getString("downloader.userAgent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36"))
				.setDefaultCookieStore(cookieStore)
				.setRetryHandler(new DefaultHttpRequestRetryHandler(1, true))
				.setMaxConnTotal(props.getInt("downloader.maxConnTotal", 1000))
				.setMaxConnPerRoute(props.getInt("downloader.maxConnPerRoute", 500))
				.build();
		
		headers.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		headers.put("Accept-Encoding","gzip, deflate, sdch");
		headers.put("Accept-Language","zh-CN,zh;q=0.8");
		headers.put("Cache-Control","max-age=0");
		
		if(StringUtils.isNotBlank(props.getString("-Cookie"))){
			String cookie = props.getString("-Cookie");
			String[] cookies = StringUtils.split(cookie, ';');
			for (String cookieItem : cookies) {
				String[] cookieKeyValue = StringUtils.split(cookieItem,"=", 2);
				if (cookieKeyValue.length>1) {
					cookieStore.addCookie(new BasicClientCookie(cookieKeyValue[0], cookieKeyValue[1]));
				}
			}
		}
		urlEscaper = Escapers.builder().addEscape(' ',"%20").addEscape('{',"%7B").addEscape('[',"%5B").addEscape(']',"%5D").addEscape('}',"%7D").build(); 
	}
	
	public Downloader keepHeader(Downloader.Header header) {
		String key = header.getName();
		String val = header.getValue();
		if (this.headers.containsKey(key))
			this.headers.put(key, this.headers.get(key) + "; " + val);
		else
			this.headers.put(key, val);
		return this;
	}

	public Downloader keepCookie(Downloader.Cookie c) {
		BasicClientCookie cookie = new BasicClientCookie(c.getName(), c.getValue());
		cookie.setDomain(c.getDomain());
		cookie.setExpiryDate(c.getExpiryDate());
		cookie.setPath(c.getPath());
		cookie.setSecure(c.isSecure());
		this.cookieStore.addCookie(cookie);
		return this;
	}

	/**
	 * 刷新当前下载器的COOKIE
	 */
	public Downloader flushCookie(String cookie) {
		cookieStore.clear();
		
		String[] cookies = StringUtils.split(cookie, ';');
		for (String cookieItem : cookies) {
			String[] cookieKeyValue = StringUtils.split(cookieItem,"=", 2);
			if (cookieKeyValue.length>1) {
				cookieStore.addCookie(new BasicClientCookie(cookieKeyValue[0], cookieKeyValue[1]));
			}
		}
		return this;
	}

	private void fixCookieDomain(String host) {
		List<org.apache.http.cookie.Cookie> cookieList = cookieStore.getCookies();
		cookieStore.clear();
		HashMap<String, org.apache.http.cookie.Cookie> cookieCache = new HashMap<>();
		for (org.apache.http.cookie.Cookie cookie : cookieList) {
			BasicClientCookie baseCookie = new BasicClientCookie(cookie.getName(), cookie.getValue());
			baseCookie.setDomain(host);
			baseCookie.setPath("/");
			cookieCache.put(cookie.getName().trim().toLowerCase(), baseCookie);
		}
		for (Entry<String, org.apache.http.cookie.Cookie> baseCookie : cookieCache.entrySet()) {
			cookieStore.addCookie(baseCookie.getValue());
		}
	}
	
	public Response download(Request request) {
		String method = request.getMethod();
		String url = request.getUrl();
		final HttpRequestBase req;
		final Response response = new Response(request);
		HttpResponse resp = null;
		try {
			url = urlEscaper.escape(url);
			if (K.HTTP_POST.equals(method)) {
				req = new HttpPost(url);
			} else {
				req = new HttpGet(url);
			}
			
			
			
			RequestConfig reqCfg = buildRequestConfig(request);
			req.setConfig(reqCfg);
			
			
			headers.forEach((k,v) ->{ if(!request.getHeaders().containsKey(k)){ req.addHeader(k, v);}});
			
			
			if (request.getHeaders() != null) {
				request.getHeaders().forEach((k,v)->req.addHeader(k,v));
			}
			if (request.getCookies() != null) {
				request.getCookies().parallelStream().forEach(c -> keepCookie(c));
			}
			
			fixCookieDomain(req.getURI().getHost());
			HttpClientContext ctx = HttpClientContext.create();
			resp = this.httpClient.execute(req, ctx);
			// get status
			StatusLine statusLine = resp.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			String statusDesc = statusLine.getReasonPhrase();
			response.setStatusCode(statusCode);
			response.setStatusDesc(statusDesc);
			// cookies
			CookieStore cs = ctx.getCookieStore();
			cs.getCookies().parallelStream()
				.map(c -> new Cookie(c.getName(), c.getValue(), c.getDomain(), c.getPath(), c.getExpiryDate(), c.isSecure()))
				.forEach(c -> keepCookie(c));
//			URLUtil.insertCookies(cs.getCookies());
			// get redirect location
			org.apache.http.Header locationHeader = resp.getFirstHeader("Location");
			if (locationHeader != null && (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY)) 
				response.setLocation(locationHeader.getValue());
			
		    // entity
			HttpEntity entity = resp.getEntity();
			// content type and charset
			ContentType contentType = ContentType.getOrDefault(entity);
			Charset charset = contentType.getCharset();
			response.setCharset(charset == null ? null : charset.name());
			response.setMimeType(contentType.getMimeType());
			// body
			byte[] body = HttpclientUtils.toByteArray(entity);
			response.setBody(body);
			resp = null;
		} catch (Throwable e) {
			response.setException(e);
			logger.error(request.toString(),e);
		} finally {  
            try {  
                if (resp != null) {  
                    resp.getEntity().getContent().close();  
                }  
            } catch (Throwable e) {  
            } 
        }  
		
		return response;
	}
	
	
	private RequestConfig buildRequestConfig(Request request) {
		RequestConfig.Builder builder = RequestConfig.copy(defaultRequestConfig);
		Properties reqProps = request;
		if (reqProps.containsKey("socketTimeout")) {
			builder.setSocketTimeout(reqProps.getInt("socketTimeout"));
		}
		if (reqProps.containsKey("connectTimeout")) {
			builder.setConnectTimeout(reqProps.getInt("connectTimeout"));
		}
		if (reqProps.containsKey("connectionRequestTimeout")) {
			builder.setConnectionRequestTimeout(reqProps.getInt("connectionRequestTimeout"));
		}
		if (reqProps.containsKey("redirectsEnabled")) {
			builder.setRedirectsEnabled(reqProps.getBoolean("redirectsEnabled"));
		}
		if (reqProps.containsKey("circularRedirectsAllowed")) {
			builder.setCircularRedirectsAllowed(reqProps.getBoolean("circularRedirectsAllowed"));
		}
		RequestConfig reqCfg = builder.build();
		return reqCfg;
	}
	
	public void close() {
		try {
			this.httpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}