package com.spiderman.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypeConverter {
	public static Date strToDate(String dateStr,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static Double strToDouble(String str) {
		try {
			return Double.parseDouble(str);
		} catch (Exception e) {
			return null;
		}
		
	}

	public static BigDecimal strToBigDecimal(String str) {
		try {
			return new BigDecimal(str);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Integer strToInteger(String str) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * unicode 转中文 只转中文而不转数字
	 * @param dataStr
	 * @return
	 */
	public static String decodeUnicode(String dataStr) {  
		 
		   try {
			// Convert from Unicode to UTF-8
			String string = dataStr;
			byte[] utf8 = string.getBytes("UTF-8");
			// Convert from UTF-8 to Unicode
			string = new String(utf8, "UTF-8");
			return dataStr;
			} catch (UnsupportedEncodingException e) {
				return null;
			}
    }
	
	public static String unicodeToString(String str) {
		 
	    Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");

	    Matcher matcher = pattern.matcher(str);
	    char ch;
	    while (matcher.find()) {
	        ch = (char) Integer.parseInt(matcher.group(2), 16);
	        str = str.replace(matcher.group(1), ch + "");    
	    }
	    return str;
	}
	
	/**
	  * unicode 转换成 utf-8
	  * @author fanhui
	  * 2007-3-15
	  * @param theString
	  * @return
	  */
	 public static String unicodeToUtf8(String theString) {
	  char aChar;
	  int len = theString.length();
	  StringBuffer outBuffer = new StringBuffer(len);
	  for (int x = 0; x < len;) {
	   aChar = theString.charAt(x++);
	   if (aChar == '\\') {
	    aChar = theString.charAt(x++);
	    if (aChar == 'u') {
	     // Read the xxxx
	     int value = 0;
	     for (int i = 0; i < 4; i++) {
	      aChar = theString.charAt(x++);
	      switch (aChar) {
	      case '0':
	      case '1':
	      case '2':
	      case '3':
	      case '4':
	      case '5':
	      case '6':
	      case '7':
	      case '8':
	      case '9':
	       value = (value << 4) + aChar - '0';
	       break;
	      case 'a':
	      case 'b':
	      case 'c':
	      case 'd':
	      case 'e':
	      case 'f':
	       value = (value << 4) + 10 + aChar - 'a';
	       break;
	      case 'A':
	      case 'B':
	      case 'C':
	      case 'D':
	      case 'E':
	      case 'F':
	       value = (value << 4) + 10 + aChar - 'A';
	       break;
	      default:
	       throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
	      }
	     }
	     outBuffer.append((char) value);
	    } else {
	     if (aChar == 't')
	      aChar = '\t';
	     else if (aChar == 'r')
	      aChar = '\r';
	     else if (aChar == 'n')
	      aChar = '\n';
	     else if (aChar == 'f')
	      aChar = '\f';
	     outBuffer.append(aChar);
	    }
	   } else
	    outBuffer.append(aChar);
	  }
	  return outBuffer.toString();
	 }

	 
}
