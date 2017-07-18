package com.spiderman.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.util.Args;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.log4j.Logger;

import net.kernal.spiderman.worker.download.DownloadWorker;
import net.kernal.spiderman.worker.download.Downloader;
import net.kernal.spiderman.worker.download.impl.HttpClientDownloader;

public class HttpclientUtils {

	static Logger logger = Logger.getLogger(HttpclientUtils.class);

	/**
	 * 本类是为了修复Httpclient 中 EntityUtils类的EOF异常无法返回字节的问题所做的升级类,本方法不vfyq1wget Read
	 * the contents of an entity and return it as a byte array.
	 *
	 * @param entity
	 *            the entity to read from=
	 * @return byte array containing the entity content. May be null if
	 *         {@link HttpEntity#getContent()} is null.
	 * @throws IOException
	 *             if an error occurs reading the input stream
	 * @throws IllegalArgumentException
	 *             if entity is null or if content length &gt; Integer.MAX_VALUE
	 */
	public static byte[] toByteArray(final HttpEntity entity) throws IOException {
		Args.notNull(entity, "Entity");
		final InputStream instream = entity.getContent();
		if (instream == null) {
			return null;
		}
		ByteArrayBuffer buffer = null;
		try {
			Args.check(entity.getContentLength() <= Integer.MAX_VALUE, "HTTP entity too large to be buffered in memory");
			int i = (int) entity.getContentLength();
			if (i < 0) {
				i = 4096;
			}
			buffer = new ByteArrayBuffer(i);
			final byte[] tmp = new byte[4096];
			int l;
			while ((l = instream.read(tmp)) != -1) {
				buffer.append(tmp, 0, l);
			}
			return buffer.toByteArray();
		} catch (EOFException e) {
			e.printStackTrace();
			return buffer.toByteArray();
		} finally {
			instream.close();
		}
	}

	private final static DownloadWorker downloadWorker = new DownloadWorker(new HttpClientDownloader());

	/**
	 * 自动重试下载网页
	 * 
	 * @param url
	 */
	public static String downloadHtmlRetry(String url) {
		return downloadHtmlRetry(url, downloadWorker);
	}

	/**
	 * 自动重试下载网页
	 * 
	 * @param url
	 */
	public static String downloadHtmlRetry(Downloader.Request request) {
		return downloadHtmlRetry(request, downloadWorker);
	}
	
	/**
	 * 自动重试下载网页
	 * 
	 * @param url
	 */
	public static String downloadHtmlRetry(String url, DownloadWorker customDownload) {
		Downloader.Request request = new Downloader.Request(url);
		
		return downloadHtmlRetry(request, customDownload);
	}

	/**
	 * 自动重试下载网页
	 * 
	 * @param url
	 */
	public static String downloadHtmlRetry(Downloader.Request request, DownloadWorker customDownload) {
		for (int retryCount = 0; retryCount < 3; retryCount++) {
			try {
				Downloader.Response response = customDownload.download(request);
				if(response.getStatusCode() == HttpStatus.SC_OK){
					return response.getBodyStr();
				}
			} catch (Exception e) {
				if (retryCount == 2)
					logger.error(String.format("数据抓取异常 重试次数:%s url:%s", retryCount, request.getUrl()), e);
				try {
					TimeUnit.MILLISECONDS.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		return null;
	}
}
