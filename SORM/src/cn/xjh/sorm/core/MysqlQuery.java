package cn.xjh.sorm.core;

import cn.xjh.po.Emp;
import cn.xjh.sorm.bean.ColumnInfo;
import cn.xjh.sorm.bean.TableInfo;
import cn.xjh.sorm.utils.JDBCUtils;
import cn.xjh.sorm.utils.ReflectUtils;
import cn.xjh.sorm.utils.StringUtils;
import cn.xjh.vo.EmpVO;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/*负责针对mysql数据库的查询*/
public class MysqlQuery extends Query {
    @Override
    public int excuteDML(String sql, Object[] params) {
        return super.excuteDML(sql, params);
    }

    @Override
    public void insert(Object obj) {
        super.insert(obj);
    }

    @Override
    public void delete(Class clazz, Object id) {
        super.delete(clazz, id);
    }

    @Override
    public void delete(Object obj) {
        super.delete(obj);
    }

    @Override
    public int update(Object obj, String[] fieldNames) {
        return super.update(obj, fieldNames);
    }

    @Override
    public List QueryRows(String sql, Class clazz, Object[] params) {
        return super.QueryRows(sql, clazz, params);
    }

    @Override
    public Object QueryUniquerow(String sql, Class clazz, Object[] params) {
        return super.QueryUniquerow(sql, clazz, params);
    }

    @Override
    public Object QueryValue(String sql, Object[] params) {
        return super.QueryValue(sql, params);
    }

    @Override
    public Number QueryNumber(String sql, Object[] params) {
        return super.QueryNumber(sql, params);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
