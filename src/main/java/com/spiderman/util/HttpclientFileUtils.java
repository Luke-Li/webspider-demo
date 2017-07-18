package com.spiderman.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

/**
 * 说明
 * 利用httpclient下载文件
 * maven依赖
 * <dependency>
*			<groupId>org.apache.httpcomponents</groupId>
*			<artifactId>httpclient</artifactId>
*			<version>4.0.1</version>
*		</dependency>
*  可下载http文件、图片、压缩文件
*  bug：获取response header中Content-Disposition中filename中文乱码问题
 * @author tanjundong
 *
 */
public class HttpclientFileUtils {

	public static final int cache = 10 * 1024;
	public static final boolean isWindows;
	public static final String splash;
	public static final String root;
	static {
		if (System.getProperty("os.name") != null && System.getProperty("os.name").toLowerCase().contains("windows")) {
			isWindows = true;
			splash = "\\";
			root="D:";
		} else {
			isWindows = false;
			splash = "/";
			root="/search";
		}
	}

	static HttpClient client = HttpClients.createDefault();
	
	/**
	 * 根据url下载文件，保存到filepath中
	 * @param httpget 必须提供
	 * @param filepath 必须提供
	 * @param filename 如不提供使用URL中的文件名称
	 * @return
	 */
	public static String download(HttpGet httpget, String filepath,String filename) {
		try {

			HttpResponse response = client.execute(httpget);

			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			if(StringUtils.isBlank(filepath))
				throw new IOException("文件保存路径不能为空值");
			
			if(StringUtils.isBlank(filename))
				filename = FilenameUtils.getName(httpget.getURI().toString());
			
			File file = Paths.get(filepath, filename).toFile();
			file.getParentFile().mkdirs();
			FileOutputStream fileout = new FileOutputStream(file);
			/**
			 * 根据实际运行效果 设置缓冲区大小
			 */
			byte[] buffer=new byte[cache];
			int ch = 0;
			while ((ch = is.read(buffer)) != -1) {
				fileout.write(buffer,0,ch);
			}
			is.close();
			fileout.flush();
			fileout.close();
			return file.toString();
		} catch (Exception e) {
			logger.error("文件下载失败:"+httpget.getURI(),e);
			return null;	
		}
		
	}
	
	static Logger logger = Logger.getLogger(HttpclientFileUtils.class);
	
	/**
	 * 获取随机文件名
	 * @return
	 */
	public static String getRandomFileName() {
		return String.valueOf(System.currentTimeMillis());
	}
	
}
