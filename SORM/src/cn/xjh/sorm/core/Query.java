package cn.xjh.sorm.core;

import cn.xjh.sorm.bean.ColumnInfo;
import cn.xjh.sorm.bean.TableInfo;
import cn.xjh.sorm.utils.JDBCUtils;
import cn.xjh.sorm.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/*对外提供服务的核心类
* @author xjh
*/

@SuppressWarnings("all")
public abstract class Query implements Cloneable{
    /*直接执行一个DML语句
    * sql：sql语句
    * params：语句内的参数
    * return 执行语句后影响了几条记录*/
    public int excuteDML(String sql,Object[] params){//执行DML语句
        int count=0;
        Connection conn=DBManger.getMysqlConn();
        PreparedStatement ps=null;
        try {
            ps=conn.prepareStatement(sql);
            //调用JDBCUtils包里的给prepareStatement传参操作
            JDBCUtils.handleParams(params,ps);
            count= ps.executeUpdate();
            System.out.println(ps);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            DBManger.close(ps,conn);
        }
        return count;
    };

    /*增加：将一个对象obj存储到数据库中
    * 把对象不为null的属性往数据库中存储，如果数字为null则放0*/
    public void insert(Object obj){//将obj新生成一个字段放入表中 insert into 表名 (id,uname,pwd) values (?,?,?)
        Class c = obj.getClass();
        List<Object> params=new ArrayList<>();//容器装对象不为null的属性的值，便于sql预处理赋值
        TableInfo tableInfo = TableContext.poClassTableMap.get(c);//获得类对应的表，得到表名
        Field fields[]=c.getDeclaredFields();//获得该类里所有的属性
        StringBuilder sql=new StringBuilder("insert into "+ tableInfo.getTname()+" (");
        int countNotNullField=0;//计算有多少个不为null的属性，方便在语句后加小问号
        for(Field f:fields){//得到obj所有属性的值
            String fieldname=f.getName();//Field的getName函数
            Object fieldvalue= ReflectUtils.invokeGet(fieldname,obj);
            if(fieldvalue!=null){sql.append(fieldname+",");
                params.add(fieldvalue);}}
        sql.setCharAt(sql.length()-1,')');//将当前字符串最后一个位置的,替换成）
        sql.append(" value (");
        for(int i=0;i<params.size();i++){sql.append("?,");}
        sql.setCharAt(sql.length()-1,')');
        excuteDML(sql.toString(),params.toArray());};

    /*删除：根据clazz类找到对应的表，以及主键id删除对应的记录*/
    public void delete(Class clazz,Object id){//id为需要删除的字段的主键的值
        //通过传入的Class对象找TableInfo
        TableInfo tableInfo= TableContext.poClassTableMap.get(clazz);//获得对应的表名
        ColumnInfo onlyPriKey=tableInfo.getOnlyPriKey();//获得主键
        String sql="delete from "+tableInfo.getTname()+" where "+onlyPriKey.getName()+"=?";
        excuteDML(sql,new Object[]{id});};

    /*删除：根据所给的对象，找到该对象的类，根据该对象的主键删除该对象在数据库表中对应的记录*/
    public void delete(Object obj){
        Class c = obj.getClass();
        TableInfo tableInfo = TableContext.poClassTableMap.get(c);
        ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();

        //通过ReflectUtils包内的反射方法，调用get方法得到该对象属性的值 找到 对应字段的主键的值
        Object priKeyvalue = ReflectUtils.invokeGet(onlyPriKey.getName(),obj);
        delete(c, priKeyvalue);//调用上面写好的函数
    };


    /*修改：更新对象指定字段的属性
    * fieldNames：需要更新的属性
    * return 更新了几行*/
    public int update(Object obj,String[] fieldNames){
        //传入obj,{"age","salary"}-->update 表名 set age=?,salary=? where id=?
        Class c = obj.getClass();
        TableInfo tableInfo = TableContext.poClassTableMap.get(c);//通过对象获得类名获得表名
        List<Object> params=new ArrayList<>();//存放该对象的属性的值
        ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();//获得主键，通过主键值删除字段
        Field fields[]=c.getDeclaredFields();
        StringBuilder sql=new StringBuilder("update "+ tableInfo.getTname()+" set ");
        for (String fieldname:fieldNames){
            Object field=ReflectUtils.invokeGet(fieldname,obj);//得到该对象属性的值
            params.add(field);
            sql.append(fieldname+"=?,");
        }
        sql.setCharAt(sql.length()-1,' ');//把逗号替换成空格
        sql.append("where "+onlyPriKey.getName()+"=?");
        params.add(ReflectUtils.invokeGet(onlyPriKey.getName(),obj));
        return excuteDML(sql.toString(),params.toArray());
    };
    /*---------------------------------------------查询----------------------------------------------------*/


    /*查询多行记录：
    * sql:查询语句
    *clazz:封装数据的java bean对类的class对象
    * params：sql的参数
    *  return 查询到的结果：一个list容器*/
    public List QueryRows(String sql,Class clazz,Object[] params){//多行多列查询
        Connection conn=DBManger.getMysqlConn();
        List<Object>list=null;//放查询结果对象的容器
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps=conn.prepareStatement(sql);
            //调用JDBCUtils包里的给prepareStatement传参操作
            JDBCUtils.handleParams(params,ps);
            rs=ps.executeQuery();
            ResultSetMetaData metaData=rs.getMetaData();//返回结果集的元数据
            //多行，每一行为一个javabean对象
            while (rs.next()){
                if(list==null){list=new ArrayList<>();}
                Object rowObj=clazz.newInstance();//创建一个javabean对象
                //多列  select empname,salary from emp where id>?
                for (int i=0;i<metaData.getColumnCount();i++){//getColumn函数得到查询语句结果集的列数，即属性数目
                    String columnName=metaData.getColumnLabel(i+1);//得到结果的列名，即属性标签名（如果没有标签则默认属性名）
                    Object columnValue=rs.getObject(i+1);

                    //调用rowObj对象的setEmpname方法，将结果集第一列的值设置进去
                    ReflectUtils.invokeSet(rowObj,columnName,columnValue);
                }
                list.add(rowObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            DBManger.close(rs,ps,conn);
        }
        return list;};

    /*查询单行记录：
     * sql:查询语句
     *clazz:封装数据的java bean对类的class对象
     * params：sql的参数
     *  return 查询到的结果：一个对象*/
    public Object QueryUniquerow(String sql,Class clazz,Object []params){
        //查询一行：直接调用查询多行的函数，返回第一行数据
        List list=QueryRows(sql,clazz,params);
        return (list!=null&&list.size()>0)?list.get(0):null;
    };

    /*查询返回一个值（一行一列）：
     * sql:查询语句
     * param：sql的参数
     *  return 查询到的结果：一个对象*/
    public Object QueryValue(String sql,Object []params){//sql查询语句，返回一个值，例如计算行数
        Connection conn=DBManger.getMysqlConn();
        Object value=null;//放查询结果对象的容器
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps=conn.prepareStatement(sql);
            //调用JDBCUtils包里的给prepareStatement传参操作
            JDBCUtils.handleParams(params,ps);
            rs=ps.executeQuery();
            ResultSetMetaData metaData=rs.getMetaData();//返回结果集的元数据
            while (rs.next()){
                value=rs.getObject(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            DBManger.close(rs,ps,conn);
        }
        return value;};

    /*根据主键获得一个字段*/
    public Object QueryById(Class clazz,Object id){
       //select* from emp where id=?
        TableInfo tableInfo=TableContext.poClassTableMap.get(clazz);//获得表信息
        ColumnInfo onlyPriKey=tableInfo.getOnlyPriKey();//获得主键
        String sql="select * from "+tableInfo.getTname()+" where "+onlyPriKey.getName()+"=? ";
       return QueryUniquerow(sql,clazz,new Object[]{id});
    }




    /*查询返回一个数字（一行一列）比如查询id：
     * sql:查询语句
     * param：sql的参数
     *  return 查询到的结果：一个对象*/
    public Number QueryNumber(String sql,Object []params){//sql语句查询结果为一个数字
        return (Number) QueryValue(sql,params);};

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
