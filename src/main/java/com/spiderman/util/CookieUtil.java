package com.spiderman.util;

import java.io.IOException;
import java.util.Properties;

/**
 * 读取配置文件中的内容
 * 
 * @author adchina
 * 
 */
public class CookieUtil
{
    private static Properties pros = new Properties();
    private static String CONFIG_FILE = "cookie.properties"; // 配置文件路径
    /**
     * 静态执行一段代码，将配置文件读入到内存中
     */
    static
    {
        load();
    }

    private static synchronized void load()
    {
        try
        {
            // 两种LOAD方式，选择一种
            // File f = new
            // File(Paths.get(System.getProperty("user.dir"),"").toString());
            // System.out.println(f.getAbsolutePath());
//            pros.load(new FileInputStream(Paths.get(FileUtil.getProgramDirectory(), CONFIG_FILE).toString()));
             pros.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(CONFIG_FILE));
            // 还有一种LOAD方式
            // ResourceBundle.getBundle("webspider.properties").getString("expectedInsertions")
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 重新加载配置文件数据
     */
    public static void reLoad()
    {
        load();
    }

    /**
     * 根据key取得对应的value
     * 
     * @param key
     *            配置文件中中的key
     * @return 配置文件中中value
     */
    public static String getProperty(String key)
    {
        return pros.getProperty(key);
    }
}