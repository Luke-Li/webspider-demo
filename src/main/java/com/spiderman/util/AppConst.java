package com.spiderman.util;

import java.io.File;

public class AppConst {
	/**
	 * 平台无关换行符
	 */
	public static final String NewLineSymbol = System.getProperty("line.separator");
	
	/**
	 * 当前操作系统名称
	 */
	public static final String OsName = System.getProperty("os.name");
	
	/**
	 * 路径分隔符
	 */
	public static final String pathSeparator = File.pathSeparator;
	
	/**
	 * 通用的分隔符
	 */
	public static final char SplitChar = ',';
	/*
	 * 网页文件的分隔符
	 */
	public static final char SplitCharWebpage = '|';
	
    //通用的全局属性字段
    public static final String ALL_VALUE = "-999";
    public static final String ALL_TEXT = "-- ALL --";
                        
    
    public static final String DateFormatFileName = "yyyyMMdd";
    public static final String ShortDateFormat = "yyyy-MM-dd";
                        
    public static final String DateFormatStandard = "yyyy-MM-dd HH:mm:ss";
    public static final String DateFormatStandardWithoutSecond = "yyyy-MM-dd HH:mm:00";

    /**
     * URL种子格式
     */
    public static final String DateFormatForUrlSeedFile = "yyyyMMddHH00";

    /**
     * http scheme
     */
    public static final String SchemeHttp = "http://";

    /**
     * safe http scheme
     */
    public static final String SchemeHttps = "https://";
    
    /**
     * 最大允许的URL长度
     */
    public static final int MaxUrlLength = 150;

    //session 相关的KEY
	public static final String Session_Admin = "Session_Admin_Key";
	
	public static final String Session_Member = "Session_Member_Key";
	/**
	 * 超级管理员的权限ID
	 */
	public static final int SupperAdminRoleId = 1;
}
