package cn.xjh.sorm.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/*封装JDBC常用操作*/
public class JDBCUtils {

    public static void handleParams(Object[] params, PreparedStatement ps){//给sql语句处理参数
        if(params!=null){//有参数才进行预处理
            for (int i=0; i <params.length; i++) {
                try {
                    ps.setObject(i+1,params[i]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }}
    }
}
