package com.spiderman.util.url;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;

import com.spiderman.util.AppConst;


public class UrlStandardization {
	static String[] DefaultDirectoryIndexes = new String[] { "index.php", "index.htm", "index.html", "default.asp",
			"default.aspx", };

	/**
	 * URL规范化
	 * 
	 * @param rootUrl
	 *            当着页面路径
	 * @param baseUrl
	 *            需要解析的URL路径
	 * @return 规范化的URL
	 */
	public static String[] Normalize(String rootUrl, String baseUrl) {
		if (StringUtils.startsWithIgnoreCase(baseUrl, AppConst.SchemeHttp)
				|| StringUtils.startsWithIgnoreCase(baseUrl, AppConst.SchemeHttps)) {
		} else {
			// 以下方法对相对路径进行转换

			try {
				URL absoluteUrl = new URL(rootUrl);
				URL parseUrl = new URL(absoluteUrl, baseUrl);
				// 最终结果
				baseUrl = parseUrl.toString();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return Normalize(baseUrl);
	}

	/// <summary>
	/// 爬虫需要两个URL是否指向相同的页面这一点可以被迅速检测出来, 这就需要URL规范化.
	/// URL规范化做的主要的事情:
	/// 转换为小写
	/// 相对URL转换成绝对URL
	/// 删除默认端口号
	/// 根目录添加斜杠
	/// 猜测的目录添加尾部斜杠
	/// 删除分块
	/// 解析路径
	/// 去除WWW
	/// 删除缺省名字
	/// 解码禁用字符
	/// 更多信息参照RFC3986:
	/// http://tools.ietf.org/html/rfc3986
	/// http://www.aijiaboke.com/it/2014/0303/125.html
	/// </summary>
	/// <param name="baseUrl">基URL，类似于当前目录</param>
	/// <param name="strUri">URL，函数会把这个URL一步一步的规范</param>
	/// <param name="needHost">这个HOST不是标准的HOST，可能经过修改</param>
	/// <returns>返回结果,第一个是规范的URL字符串,第二个是爬虫系统中的HOST，第三个是爬虫系统中的PATH</returns>
	public static String[] Normalize(String baseUrl) {
		// 删除默认端口号
		// 解析路径
		// 解码转义字符

		String needHost;

		if (StringUtils.isBlank(baseUrl))
			return null;

		URI tempUri;

		try {
			// 如果baseUrl是中文，空格或一些其它字符，new URI方法会抛异常。我们需要先处理一下

			URL url = new URL(baseUrl);
			tempUri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
			tempUri = tempUri.normalize();

			// 这里需要测试一下URL是否规范
			String strUri = tempUri.toString();

			// 删除分块
			if (!StringUtils.isEmpty(tempUri.getRawFragment())) {
				strUri = strUri.replace("#" + tempUri.getRawFragment(), StringUtils.EMPTY);
			}

			// 当查询字符串为空时去掉问号"?",并删除无意义的URL参数
			if (!StringUtils.isEmpty(tempUri.getQuery())) {
				HashMap<String, String> result = URLUtil.ParseQueryString(tempUri.getQuery());

				if (0 == result.size()) {
					strUri = strUri.replace(tempUri.getQuery(), StringUtils.EMPTY);
				} else if (1 == result.size()) {
					String key = result.keySet().iterator().next();

					if (StringUtils.isBlank(key) || StringUtils.isBlank(result.get(key))) {
						strUri = strUri.replace("?" + tempUri.getRawQuery(), StringUtils.EMPTY);
					}
				}
			}

			// 根目录添加斜杠
			/*
			if (!strUri.endsWith("/")) {
				int posTailingSlash = strUri.indexOf("/", 8);

				if (posTailingSlash == -1)
					strUri += '/';
				else if (StringUtils.isBlank(tempUri.getQuery()) && strUri.indexOf(".", posTailingSlash) == -1) {
					// 猜测的目录添加尾部斜杠
					strUri += '/';
				}
			}*/

			
			/**
			// 删除WWW
			String topDomain = GetTopLevelDomain(tempUri.getHost());
			if (IsWWWDomain(tempUri.getHost(), topDomain)) {
				strUri = StringUtils.replaceOnce(strUri, "www.", StringUtils.EMPTY);
				// strUri = StringUtils.removeStart(strUri, "www.");

				// strUri = strUri.Remove(tempUri.getScheme().length()+3,4);
				needHost = StringUtils.removeStart(tempUri.getHost(), "www.");
				// needHost = tempUri.Host.Remove(0, 4);
			} else {
				needHost = tempUri.getHost();
			}**/
			
			needHost = tempUri.getHost();

			// 删除缺省名字
			for (String index : DefaultDirectoryIndexes) {
				if (strUri.endsWith(index)) {
					strUri = strUri.substring(0, (strUri.length() - index.length()));
					break;
				}
			}

			String fullPath = StringUtils.substring(strUri, tempUri.getScheme().length() + 3 + needHost.length());
			String[] urlStrings = { strUri, needHost, fullPath };

			return urlStrings;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	static ArrayList<String> m_domainArray = new ArrayList<String>(Arrays.asList(".com.cn", ".net.cn", ".org.cn",
			".gov.cn", ".co.jp", ".com.mx", ".com.hk", ".com.ag", ".net.ag", ".com.br", ".net.br", ".com.bz", ".net.bz",
			".com.co", ".net.co", ".nom.co", ".com.es", ".nom.es", ".org.es", ".co.nz", ".net.nz", ".org.nz", ".com.tw",
			".idv.tw", ".org.tw", ".co.uk", ".me.uk", ".org.uk", ".org.ag", ".co.in", ".firm.in", ".gen.in", ".ind.in",
			".net.in", ".ac.cn", ".edu.cn", ".org.in",

			".com", ".cn", ".mobi", ".tel", ".asia", ".net", ".org", ".name", ".me", ".tv", ".cc", ".hk", ".biz",
			".info", "", ".公司", ".中国", ".网络", ".co", ".us", ".xxx", ".ca", ".mx", ".ws", ".ag", ".am", ".at", ".be",
			".bz", ".de", ".es", ".eu", ".fm", ".fr", ".gs", ".in", ".it", ".jobs", ".jp", ".ms", ".nl", ".nu", ".se",
			".tc", ".tk", ".tw", ".vg", ".edu", ".ac", ".io", ".la", ".md", ".pl", ".ru", ".sc", ".sg", ".sh", ".uk",
			".vc", ".il", ".li", ".nz"));

	static HashSet<String> m_domainSet = new HashSet<String>(m_domainArray);

	static char[] CompartArray = new char[] { '.' };

	/// <summary>
	/// 根据HOST获取URL的顶级域名(根域名)
	/// </summary>
	/// <param name="host"></param>
	/// <returns></returns>
	public static String GetTopLevelDomain(String host) {
		String[] hs = host.split("\\.");

		if (hs.length > 2) {
			// 传入的host地址至少有三段
			int lastPointIndex = host.lastIndexOf('.');// 最后一次“.”出现的位置
			int lastbutonePointIndex = host.substring(0, lastPointIndex).lastIndexOf('.');// 倒数第二个“.”出现的位置
			String guessExt = host.substring(lastbutonePointIndex);
			if (!m_domainSet.contains(guessExt)) {
				return StringUtils.removeStart(guessExt, ".");
				// return guessExt.trimStart('.');
			}

			// 域名后缀为两段（有用“.”分隔）
			if (hs.length > 3)
				return host.substring(host.substring(0, lastbutonePointIndex).lastIndexOf('.') + 1);
			else
				return StringUtils.removeStart(guessExt, ".");
			// return host.TrimStart('.');
		} else if (hs.length == 2) {
			return StringUtils.removeStart(host, ".");
			// return host.TrimStart('.');
		} else {
			return StringUtils.EMPTY;
		}
	}

	/// <summary>
	/// 是否是以标准的WWW开头的域,如 www.abc.com www.abc.com.cn 而
	/// www.a.abc.com.cn这种类型的就不是标准的URL
	/// </summary>
	/// <param name="domain"></param>
	/// <param name="topLevelDomain"></param>
	/// <returns></returns>
	public static Boolean IsWWWDomain(String domain, String topLevelDomain) {
		return domain.equalsIgnoreCase("www." + topLevelDomain);
	}

	static void println(String[] args) {
		System.out.println(MessageFormat.format("url:{0}  host:{1}  path:{2}", args[0], args[1], args[2]));
	}

	public static void main(String[] args) throws IOException {

		println(Normalize("http://www.dzwww.com/yule/yulezhuanti/mtcbg/201312/t20131205_9202648.htm"));

		println(Normalize("http://so.55bbs.com/search.php?q=普吉 卡伦&p=2&s=5946671531345126826&nsid=1"));
		println(Normalize("http://fanyi.baidu.com/#zh/en/%E8%A7%84%E8%8C%83"));
		println(Normalize("https://www.google.com.hk/webhp?hl=zh-CN&key=%E8%A7%84%E8%8C%83"));
		println(Normalize("http://www.oschina.net/project/tag/64/spider?sort=view&lang=19&os=0"));
		println(Normalize("http://view.news.qq.com/original/intouchtoday/n2802.html"));
		println(Normalize("http://www.qq.com/?a="));
		println(Normalize("http://www.qq.com/"));
		println(Normalize("http://www.qq.com"));
		println(Normalize("http://sports.qq.com/others/2014tucup.htm"));
	}
}
