package cn.xjh.sorm.utils;

import cn.xjh.sorm.bean.ColumnInfo;
import cn.xjh.sorm.bean.JavaFieldGetSet;
import cn.xjh.sorm.bean.TableInfo;
import cn.xjh.sorm.core.DBManger;
import cn.xjh.sorm.core.MysqlTypeConvertor;
import cn.xjh.sorm.core.TableContext;
import cn.xjh.sorm.core.TypeConvertor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*封装生成java类文件（源代码）的常用操作*/
public class javaFileUtils {
    /*根据字段信息生成java属性信息。如：var username-->生成private String username;以及该属性的set，get方法
     * column:字段信息
     * convertor：数据类型转换器
     * return java属性和set/get方法源码*/
    public static JavaFieldGetSet createJavaFieldGetSetSRC(ColumnInfo column, TypeConvertor convertor) {
        JavaFieldGetSet jfgs = new JavaFieldGetSet();//该类封装java属性和get，set方法源代码
        String javaFieldType = convertor.databaseType2javaType(column.getDataType());
        jfgs.setFieldInfo("\t" + "private " + javaFieldType + " " + column.getName() + ";\n");

        //生成getter的源代码,例如 public int getID(){ return id;}
        StringBuilder getSrc = new StringBuilder();//StringBuilder便于拼接字符串
        getSrc.append("\tpublic " + javaFieldType + " get" + StringUtils.firstChar2UpperCase(column.getName()) + "(){\n");
        getSrc.append("\treturn " + column.getName() + ";}\n");
        jfgs.setGetInfo(getSrc.toString());

        //生成setter的源代码,例如 public int getID(){ return id;}
        StringBuilder setSrc = new StringBuilder();//StringBuilder便于拼接字符串
        setSrc.append("\tpublic void set" + StringUtils.firstChar2UpperCase(column.getName()) + "(" + javaFieldType + " " + column.getName() + "){\n");
        setSrc.append("\tthis." + column.getName() + "=" + column.getName() + ";}\n");
        jfgs.setSetInfo(setSrc.toString());

        return jfgs;
    }

    /*根据表信息生成java类的源代码
     * tableInfo:表信息
     * convertor：数据类型转换器
     * return java类的源代码*/
    public static String createJavaSrc(TableInfo tableInfo, TypeConvertor convertor) {
        StringBuilder src = new StringBuilder();
        Map<String, ColumnInfo> columns = tableInfo.getColumns();
        List<JavaFieldGetSet> javaFields = new ArrayList<JavaFieldGetSet>();//存储表内所有的字段

        for (ColumnInfo c : columns.values()) {
            javaFields.add(createJavaFieldGetSetSRC(c, convertor));
        }
//开始拼接源码
//生成package语句
        src.append("package " + DBManger.getConf().getPopackage() + ";\n\n");//通过DBManger类中的Conf对象得到配置文件写好的存放类的位置
//生成import语句
        src.append("import java.sql.*;\n");
        src.append("import java.util.*;\n\n\n");
//生成类声明语句
        src.append("public class " + StringUtils.firstChar2UpperCase(tableInfo.getTname()) + " {\n");
//生成属性列表
        for (JavaFieldGetSet f : javaFields) {
            src.append(f.getFieldInfo());//调用上面的写好的方法得到属性信息
        }
        src.append("\n\n");//空行
//生成get方法列表
        for (JavaFieldGetSet f : javaFields) {
            src.append(f.getGetInfo());//调用上面的写好的方法得到属性信息
        }
//生成set方法列表
        for (JavaFieldGetSet f : javaFields) {
            src.append(f.getSetInfo());//调用上面的写好的方法得到属性信息
        }
//生成类结束语句
        src.append("}\n");

        return src.toString();
    }

    public static void createJavaPoFile(TableInfo tableInfo,TypeConvertor convertor) throws IOException {
          String src=createJavaSrc(tableInfo, convertor);//调用上面的方法生成一张表对应的java类的源代码
        String secPath=DBManger.getConf().getSrc()+"/";//得到配置文件内的src文件的地址
        String packagePath=DBManger.getConf().getPopackage().replaceAll("\\.","\\\\");//得到配置文件内po包的地址，在java内一个反斜杠都要写成两个
        File f=new File(secPath+"\\"+packagePath);
       if(!f.exists()){
          f.mkdir();
       }
        BufferedWriter bw=null;//通过流将源码写入文件
        bw=new BufferedWriter(new FileWriter(f.getAbsoluteFile()+"\\"+StringUtils.firstChar2UpperCase(tableInfo.getTname())+".java",false));
        try {
            bw.write(src);
            System.out.println("建立表"+tableInfo.getTname()+"对应的java类"+StringUtils.firstChar2UpperCase(tableInfo.getTname())+".java"+"到指定目录下");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bw!=null){
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

   public static void main(String[] args) throws IOException {//测试代码
      //  ColumnInfo ci = new ColumnInfo("id", "tinyint", 0);
     //   JavaFieldGetSet j = createJavaFieldGetSetSRC(ci, new MysqlTypeConvertor());
       // j.toString()
       Map<String,TableInfo>map= TableContext.tables; //循环获得表内容
       for(String Tablename:map.keySet()){  TableInfo t=map.get(Tablename);
           createJavaPoFile(t,new MysqlTypeConvertor());//根据表内容新建类，将该代码写成方法放入TableContext类中}
    }
}

}