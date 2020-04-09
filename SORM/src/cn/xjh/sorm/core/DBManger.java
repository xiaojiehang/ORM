package cn.xjh.sorm.core;

import cn.xjh.sorm.bean.Configuration;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/*根据配置信息，维持连接对象的 管理（增加连接池）*/
public class DBManger {
    private static Configuration conf;
    static {//加载配置文件，封装成一个对象
        Properties pros=new Properties();//读取配置资源文件
        try {
            pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));//读取bin目录下资源
        } catch (IOException e) {
            e.printStackTrace();
        }
        conf=new Configuration();
        conf.setDriver(pros.getProperty("driver"));
        conf.setUrl(pros.getProperty("url"));
        conf.setUser(pros.getProperty("user"));
        conf.setPassword(pros.getProperty("password"));
        conf.setSrc(pros.getProperty("src"));
        conf.setPopackage(pros.getProperty("popackage"));
        conf.setUsingdb(pros.getProperty("usingdb"));
        conf.setQueryClass(pros.getProperty("queryClass"));
    }
/*获得Connection对象*/
    public static Connection getMysqlConn() {//连接数据库
        try {
            Class.forName(conf.getDriver());//得到驱动
            String url = conf.getUrl();
            return DriverManager.getConnection(url, conf.getUser(), conf.getPassword());//建立连接
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /*关闭传入的ResultSet Statement Connection对象*/
    public static void close(ResultSet rs, Statement ps, Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*关闭传入的 Statement Connection对象*/
    public static void close(Statement ps, Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*关闭传入的Connection对象*/
    public static void close( Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*返回Configuration对象*/
    public static Configuration getConf(){
          return conf;
    }
}
