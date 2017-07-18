package com.spiderman.util;

import org.mozilla.universalchardet.UniversalDetector;

public class EncoderDetector {
	/*
	 * 自动返回编码的类
	 * */
	public static String detect(byte[] content) {

		UniversalDetector detector = new UniversalDetector(null);

		// 开始给一部分数据，让学习一下啊，官方建议是1000个byte左右（当然这1000个byte你得包含中文之类的）

		detector.handleData(content, 0, content.length);

		// 识别结束必须调用这个方法

		detector.dataEnd();

		// 神奇的时刻就在这个方法了，返回字符集编码。

		return detector.getDetectedCharset();
	}
	
	/**
	 * gb2312,gbk和gb18030都是中文编码
	 * 这个函数的作用就是判断传进的编码是不是中文编码
	 * @param charset
	 * @return
	 */
	public static boolean IsGbk(String charset)
	{
	    switch (charset)
        {
            case "gbk":
                return true;
            case "gb2312":
                return true;
            case "gb18030":
                return true;
                default :
                    return false;
        }
	}
}
