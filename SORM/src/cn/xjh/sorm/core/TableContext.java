package cn.xjh.sorm.core;

import cn.xjh.sorm.bean.ColumnInfo;
import cn.xjh.sorm.bean.TableInfo;
import cn.xjh.sorm.utils.StringUtils;
import cn.xjh.sorm.utils.javaFileUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*负责获取管理数据库所有表结构和类结构的关系，并可以根据表结构生成类结构*/
public class TableContext {
    /*表名为key，表信息对象为value*/
    public static Map<String, TableInfo> tables=new HashMap<String, TableInfo>();//启动时就加载数据库的表
   /*将po的class对象和表信息对象关联起来，便于重用*/
    public static Map<Class,TableInfo> poClassTableMap=new HashMap<Class, TableInfo>();//在启动的时候将生成Class对象与其在数据库的表对应起来

    private TableContext(){};
    /*初始化，获得数据库元数据，得到数据库表的信息*/
    static {
        try {
            Connection con = DBManger.getMysqlConn();
            DatabaseMetaData dbmd = con.getMetaData();//DatabaseMetaData类是java.sql包中的类，可以获取我们连接到的数据库的结构、存储等很多信息
            ResultSet rs = dbmd.getTables("sorm", "sorm", "%", new String[]{"TABLE"});
            /*getTables()方法，该方法需要传进4个参数：数据库名称(若不传入数据库名称sorm则会读取所有数据库的表),模式,表名,类型标准*/
            while (rs.next()) {
                String tableName = (String) rs.getObject("TABLE_NAME");
                TableInfo ti = new TableInfo(tableName, new HashMap<String, ColumnInfo>(), new ArrayList<ColumnInfo>());
                tables.put(tableName, ti);
                ResultSet set = dbmd.getColumns(null, "%", tableName, "%");
                while (set.next()) {
                    ColumnInfo ci = new ColumnInfo(set.getString("COLUMN_NAME"), set.getString("TYPE_NAME"), 0);
                    ti.getColumns().put(set.getString("COLUMN_NAME"), ci);
                }
                ResultSet keySet = dbmd.getPrimaryKeys(null, "%", tableName);
                while (keySet.next()) {
                    ColumnInfo ci2 = (ColumnInfo) ti.getColumns().get(keySet.getString("COLUMN_NAME"));
                    ci2.setKeyType(1);
                    ti.getPriKeys().add(ci2);
                }
                if(ti.getPriKeys().size()>0){//去唯一主键，方便使用
                    ti.setOnlyPriKey(ti.getPriKeys().get(0));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            UpdatePoFile();//根据数据库的表生成PO包下的类
            loadPoTables();//加载这些类，并与表绑定起来，便于操作
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*静态方法：调用该方法根据获得数据库表结构，更新配置po包下的类*/
    public static void UpdatePoFile() throws IOException {
        Map<String, TableInfo> map = TableContext.tables; //循环获得表内容
        for (String Tablename : map.keySet()) {
            TableInfo t = map.get(Tablename);
            javaFileUtils.createJavaPoFile(t, new MysqlTypeConvertor());
        }
    }

    public static void loadPoTables(){
        for(TableInfo tableInfo:tables.values()){
            try {
                Class c=Class.forName(DBManger.getConf().getPopackage()+"."+ StringUtils.firstChar2UpperCase(tableInfo.getTname()));//让jvm加载po包的类
                poClassTableMap.put(c,tableInfo);//将生成的类与数据库的表关联起来
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Map<String,TableInfo> tables=TableContext.tables;
        System.out.println(tables);
    }

}
