package com.spiderman.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class WordSegmentUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(WordSegmentUtil.class);

	private static final String ltp_url = "http://192.168.0.67:12345/ltp";
	private static final String b = "b";
	//private static final String ws = "ws";
	private static final String pos = "pos";
	

	public static List<NameValuePair> getPara(String text, String format, String func) {
		List<NameValuePair> res = new ArrayList<NameValuePair>();
		res.add(new BasicNameValuePair("s", text));
		res.add(new BasicNameValuePair("x", format));
		res.add(new BasicNameValuePair("t", func));

		return res;
	}

	/**
	 * <?xml version="1.0" encoding="utf-8" ?>
	 * <xml4nlp> <note sent="y" word="y" pos="n" ne="n" parser="n" wsd="n" srl=
	 * "n" /> <doc> <para id="0"> <sent id="0" cont="财富管理"> <word id="0" cont=
	 * "财富" /> <word id="1" cont="管理" /> </sent> </para> </doc> </xml4nlp>
	 * 
	 * @param xml
	 * @return
	 */
	private static List<String> parseXml(String xml){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();      
        DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			
			Document doc = db.parse(new InputSource(new StringReader(xml)));  
			
			List<String> wordList = new ArrayList<>();
			Element eleRoot = doc.getDocumentElement();
			NodeList nodes = eleRoot.getElementsByTagName("word");

			for (int i = 0; i < nodes.getLength(); i++) {
				String pos = ((Element) nodes.item(i)).getAttribute("pos");
				if(pos.startsWith("n")){
					String cont = ((Element) nodes.item(i)).getAttribute("cont");
					wordList.add(cont);
				}
			}
			
			return wordList;
		} catch (Exception e) {
			LOGGER.error("XML解析错误",e);
			return null;
		}  
	}

	public static List<String> getWordSegment(String key) {
		List<String> wordList = null;
		CloseableHttpResponse resp = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost hp = new HttpPost(ltp_url);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params = getPara(key, b, pos);

			hp.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
			resp = httpclient.execute(hp);

			HttpEntity ent = resp.getEntity();
			String result = EntityUtils.toString(ent);
			
			result = new String(result.getBytes("ISO-8859-1"), "UTF-8");
			System.out.println(result);
			//EntityUtils.consume(ent);
			wordList = parseXml(result);
		} catch (Exception e) {
			LOGGER.error("分词错误", e);
		} finally {
			try {
				httpclient.close();
				
				if (resp != null) {
					resp.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return wordList;
	}

	public static void main(String[] args) throws Exception{
		List<String> wordList = getWordSegment("巨灾险“破冰” 产品待细分  业绩趋缓应收高企 博思软件股权分散小心“野蛮人”  打工皇帝唐骏“暗度A股”计划泡汤  退市已成定局 亿元买盘豪赌*欣泰  股权争夺莫触法律红线 选择投资谨防一哄而散  央企改革重组步伐加快 _中国经济网——国家经济门户");
		for (String word : wordList) {
			System.out.println(word);
		}
		
		//parseXml(FileUtil.readContent("C:\\Users\\Administrator\\Desktop\\test.xml", "utf-8"));
		
		/*
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new FileReader(new File("e:/ltpClient.txt")));
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line).append("\n");
		}
		br.close();
		List<String> wordList = parseXml(sb.toString());
		for (String word : wordList) {
			System.out.println(word);
		}*/
	}
}
