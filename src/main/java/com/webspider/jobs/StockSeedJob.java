package com.webspider.jobs;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.spiderman.util.HttpclientUtils;
import com.spiderman.util.TimeUtil;

import net.kernal.spiderman.worker.download.Downloader;

public class StockSeedJob {

	Logger logger = Logger.getLogger(getClass());

	public static String[] reportTypes = { "T004001001", "T004001002", "T004001003", "T004001004", "T013001001",
			"T013001002", "T013001003", "T013001004" };

	public static String thirdBoardName = "ChoiceThirdboard";
	public static String aStockName = "ChoiceAStock";

	public static void main(String[] args) {
		Map<String, String> params = new HashMap<>();

		new StockSeedJob().execute(params);
	}

	private Downloader.Request getRequest(String url, String referer) {

		if (StringUtils.isEmpty(referer)) {
			referer = "http://app.jg.eastmoney.com/html_Notice/index.html";
		}

		Downloader.Request request = new Downloader.Request(url);

		request.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		// request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64)
		// AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.59
		// Safari/537.36");
		request.addHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.94 Safari/537.36");
		request.addHeader("X-Requested-With", "XMLHttpRequest");
		request.addHeader("Referer", referer);
		request.addHeader("Accept-Encoding", "gzip,deflate");
		request.addHeader("Accept-Language", "en-us,en");

		return request;
	}

	public void execute(Map<String, String> params) {
		try {
			for (String type : reportTypes) {
				handleStock(type);
			}

			System.exit(0);

		} catch (Exception e) {
			logger.error(e.getStackTrace());
		}

	}

	/**
	 * 处理逻辑 获取前一天的公告
	 * 
	 * @param stock
	 */
	private synchronized void handleStock(String reportType) {
		try {
			String url = "http://app.jg.eastmoney.com/Notice/GetNoticeById.do?id=%s&pageIndex=%d&limit=20&sort=date&order=desc";
			boolean nextPage = true;
			String currentDate = TimeUtil.getDateBeforeDays(1);// 获取一天以前的数据
			int page = 1;

			while (nextPage) {
				Downloader.Request request = getRequest(String.format(url, reportType, page), null);
				String jsonHtml = HttpclientUtils.downloadHtmlRetry(request);
				page++;

				if (!StringUtils.isEmpty(jsonHtml)) {
					JSONObject baseObject = JSONObject.parseObject(jsonHtml);
					JSONArray records = baseObject.getJSONArray("records");

					for (int j = 0; j < records.size(); j++) {
						JSONObject record = records.getJSONObject(j);
						String date = record.getString("date");

						if (currentDate.compareToIgnoreCase(date) > 0) {
							// 时间已经比昨天早，直接break
							nextPage = false;
							break;
						} else if (currentDate.compareToIgnoreCase(date) < 0) {
							// 时间比昨天新，暂不统计
							continue;
						}

						// 时间就是昨天的
						JSONArray secuList = record.getJSONArray("secuList");

						for (int k = 0; k < secuList.size(); k++) {
							JSONObject secu = secuList.getJSONObject(k);
							String secuName = secu.getString("secuSName");
							String secuCode = secu.getString("secuFullCode");

							System.out.println(secuName + " " + secuCode);
						}

					}
				} else {
					nextPage = false;
				}
			}
		} catch (Exception e) {

			logger.error(e);
		}

	}

}
