package com.spiderman.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StringUtil {
	private static Log log = LogFactory.getLog(StringUtil.class);
	
	/**
	 * 解码Unicode编码的字符串
	 * @param dataStr
	 * @return
	 */
	public static String decodeUnicode(final String dataStr) {
		
		if(StringUtils.isBlank(dataStr))return dataStr;
		
		StringBuilder sb = new StringBuilder();
		char[] charArray = dataStr.toCharArray();
		
		int start = 0;
		int end = charArray.length;
		for (; start < end; start++) {
			char currentChar = charArray[start];
			if((end - start)>5){
				if(currentChar == '\\' && charArray[start+1] == 'u'){
					//0-9a-fA-F  48-57  97-122  65-90
					boolean isNumber = true;
					char[] unicodeArrays = new char[4];
					for (int i = 0; i < 4; i++) {
						unicodeArrays[i] = charArray[start+2+i];
						if((unicodeArrays[i]>=48 && unicodeArrays[i]<=57) || (unicodeArrays[i]>=65 && unicodeArrays[i]<=90) || (unicodeArrays[i]>=97 && unicodeArrays[i]<=122)){
							
						}else{
							isNumber = false;
							break;
						}
					}
					if(isNumber){
						sb.append((char)Integer.parseInt(new String(unicodeArrays),16));
						start+=5;
						continue;
					}
				}
			}
			sb.append(currentChar);
		}

		return sb.toString();
	}
	
    /**
     * 根据Unicode编码完美的判断中文汉字和符号
     * @param c
     * @return
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }
 
    /**
     * 完整的判断中文汉字和符号
     * @param strName
     * @return
     */
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }
	
    /**
     * 将驼峰风格替换为下划线风格
     */
    public static String camelhumpToUnderline(String str) {
        final int size;
        final char[] chars;
        final StringBuilder sb = new StringBuilder(
                (size = (chars = str.toCharArray()).length) * 3 / 2 + 1);
        char c;
        for (int i = 0; i < size; i++) {
            c = chars[i];
            if (isUppercaseAlpha(c)) {
                sb.append('_').append(toLowerAscii(c));
            } else {
                sb.append(toUpperAscii(c));
            }
        }
        return sb.charAt(0) == '_' ? sb.substring(1) : sb.toString();
    }

    /**
     * 将下划线风格替换为驼峰风格
     */
    public static String underlineToCamelhump(String str) {
        Matcher matcher = Pattern.compile("_[a-z]").matcher(str);
        StringBuilder builder = new StringBuilder(str);
        for (int i = 0; matcher.find(); i++) {
            builder.replace(matcher.start() - i, matcher.end() - i, matcher.group().substring(1).toUpperCase());
        }
        if (Character.isUpperCase(builder.charAt(0))) {
            builder.replace(0, 1, String.valueOf(Character.toLowerCase(builder.charAt(0))));
        }
        return builder.toString();
    }

    public static boolean isUppercaseAlpha(char c) {
        return (c >= 'A') && (c <= 'Z');
    }

    public static boolean isLowercaseAlpha(char c) {
        return (c >= 'a') && (c <= 'z');
    }

    public static char toUpperAscii(char c) {
        if (isUppercaseAlpha(c)) {
            c -= (char) 0x20;
        }
        return c;
    }

    public static char toLowerAscii(char c) {
        if (isUppercaseAlpha(c)) {
            c += (char) 0x20;
        }
        return c;
    }
    
    /**
	 * 将给定字符串转为int型
	 *
	 * @param s
	 * @param defaultValue
	 *            转换失败时的默认值
	 * @return
	 */
	public static int parseInt(String s, int defaultValue)
	{
		return parseInt(s, defaultValue, "");
	}

	/**
	 * 将给定字符串转为int型
	 *
	 * @param s
	 * @param defaultValue
	 *            转换失败时的默认值
	 * @param customLog
	 *            转换失败时自定义log
	 * @return
	 */
	public static int parseInt(String s, int defaultValue, String customLog)
	{
		int result = defaultValue;
		try
		{
			result = Integer.parseInt(s);
		}
		catch (Exception e)
		{
			if (null != customLog && !"".equals(customLog))
			{
				log.debug(customLog);
			}
			result = defaultValue;
		}
		return result;
	}
}
