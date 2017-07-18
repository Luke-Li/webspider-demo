package com.spiderman.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class FileUtil {

	public static void main(String[] args) {
		/*Path seedFile = Paths.get("C:\\Users\\Administrator\\Desktop\\abc.txt");
		 try(FileInputStream inputStream = new FileInputStream(seedFile.toFile());
             	InputStreamReader streamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
             	BufferedReader reader = new BufferedReader(streamReader);)
         {
             String urlOriginal;

             while ((urlOriginal = reader.readLine()) != null)
             {
            	 System.out.println(urlOriginal);
             }
             
             
             //关闭文件流
             reader.close();
             streamReader.close();
             inputStream.close();
             
             Files.delete(seedFile);
             
         }catch (Exception e) {
        	 e.printStackTrace();
		}*/
		
		getLocalIp();
		
	}
	
	
    /**
     * 获取程序的目录
     * @return
     */
    public static String getProgramDirectory()
    {
        //还有一种获取当前文件夹的方式：File directory = new File("");//设定为当前文件夹
        return System.getProperty("user.dir");
    }
    
    public static void writeContent(String path,String charset,String content) {
    	try {
    		FileWriter fw = new FileWriter(path);
    		fw.write(content);
    		fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public static String getLocalIp() {
    	
    	try {
    		InetAddress ia=InetAddress.getLocalHost();
    		return ia.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    	return null;
	}
    
	public static String readContent(String path,String charset) {
        try
        {
            InputStreamReader isReader = new InputStreamReader(new FileInputStream(path), charset);
            
            BufferedReader reader = new BufferedReader(isReader);
            String aLine;
            StringBuilder sb = new StringBuilder();
    
            while ((aLine = reader.readLine()) != null) {
                sb.append(aLine + " ");
            }
            isReader.close();
            reader.close();
            return sb.toString();
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }

	    
	    
	    
		/*List<String> lines;
		try {
			lines = Files.readAllLines(Paths.get(path),Charset.forName(charset));
			StringBuilder sBuilder = new StringBuilder();
			for (String line : lines) {
				sBuilder.append(line);
			}			
			return sBuilder.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		return null;
	}
}
