package com.spiderman.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlHelper {
	 // 定义要使用的变量
    private static Connection conn = null;
    private static PreparedStatement ps = null;
    private static ResultSet rs = null;
    private static CallableStatement cs = null;

    private static String driver = "com.mysql.jdbc.Driver";
    private static String url = "jdbc:mysql://192.168.0.66:3306/web_db?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull";
    private static String userName = "root";
    private static String password = "password";

    public static Connection getConn() {
        return conn;
    }

    public static PreparedStatement getPs() {
        return ps;
    }

    public static ResultSet getRs() {
        return rs;
    }
   
    public static CallableStatement getCs() {
        return cs;
    }

    // 加载驱动，只需要一次
    static {
        try {
            Class.forName(driver);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    // 得到连接
    public static Connection getConnection() {
        try {
            conn = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    // 处理多个update/delete/insert
    public static void executeUpdateMultiParams(String[] sql,
            String[][] parameters) {
        try {
            // 获得连接
            conn = getConnection();
            // 可能传多条sql语句
            conn.setAutoCommit(false);
            for (int i = 0; i < sql.length; i++) {
                if (parameters[i] != null) {
                    ps = conn.prepareStatement(sql[i]);
                    for (int j = 0; j < parameters[i].length; j++)
                        ps.setString(j + 1, parameters[i][j]);
                }
                ps.executeUpdate();
            }
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new RuntimeException(e.getMessage());
        } finally {
            // 关闭资源
            close(rs, ps, conn);
        }
    }

    // update/delete/insert
    // sql格式:UPDATE tablename SET columnn = ? WHERE column = ?
    public static void executeUpdate(String sql, Object[] parameters) {
        try {
            // 1.创建一个ps
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            // 给？赋值
            if (parameters != null)
                for (int i = 0; i < parameters.length; i++) {
                	ps.setObject(i + 1, parameters[i]);
                    //ps.setString(i + 1, parameters[i]);
                }
            // 执行
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();// 开发阶段
            throw new RuntimeException(e.getMessage());
        } finally {
            // 关闭资源
            close(rs, ps, conn);
        }
    }

    // select
    public static ResultSet executeQuery(String sql, Object[] parameters) {
        ResultSet rs = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    ps.setObject(i + 1, parameters[i]);
                }
            }
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {

        }
        return rs;
    }

    // 调用无返回值存储过程
    // 格式： call procedureName(parameters list)
    public static void callProc(String sql, String[] parameters) {
        try {
            conn = getConnection();
            cs = conn.prepareCall(sql);
            // 给？赋值
            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++)
                    cs.setObject(i + 1, parameters[i]);
            }
            cs.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            // 关闭资源
            close(rs, cs, conn);
        }
    }

    // 调用带有输入参数且有返回值的存储过程
    public static CallableStatement callProcInput(String sql, String[] inparameters) {
        try {
            conn = getConnection();
            cs = conn.prepareCall(sql);
            if(inparameters!=null)
                for(int i=0;i<inparameters.length;i++)
                    cs.setObject(i+1, inparameters[i]);               
            cs.execute();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }finally{
           
        }
        return cs;
    }
   
    // 调用有返回值的存储过程
    public static CallableStatement callProcOutput(String sql,Integer[] outparameters) {
        try {
            conn = getConnection();
            cs = conn.prepareCall(sql);                   
            //给out参数赋值
            if(outparameters!=null)
                for(int i=0;i<outparameters.length;i++)
                    cs.registerOutParameter(i+1, outparameters[i]);
            cs.execute();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }finally{
           
        }
        return cs;
    }

    public static void close(ResultSet rs, Statement ps, Connection conn) {
        if (rs != null)
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        rs = null;
        if (ps != null)
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        ps = null;
        if (conn != null)
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        conn = null;
    }
}
