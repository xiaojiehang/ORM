package cn.xjh.sorm.core;
/*mysql数据库和java数据类型转换，继承TypeConvertor接口*/
public class MysqlTypeConvertor implements TypeConvertor {

    @Override
    public String databaseType2javaType(String columnType) {
        if(columnType.equalsIgnoreCase("varchar")||columnType.equalsIgnoreCase("char")){//将数据库的varchar类型转为java的String
            return "String";
        }else if(columnType.equalsIgnoreCase("int")||
                columnType.equalsIgnoreCase("tinyint")||
                columnType.equalsIgnoreCase("smallint")||
                columnType.equalsIgnoreCase("integer")){return "Integer";}
        else if(columnType.equalsIgnoreCase("bigint")){return "Long";}
        else if(columnType.equalsIgnoreCase("double")||columnType.equalsIgnoreCase("float")){return "Double";}
        else if(columnType.equalsIgnoreCase("clob")){return "java.sql.Clob";}
        else if(columnType.equalsIgnoreCase("blob")){return "java.sql.Blob";}
        else if(columnType.equalsIgnoreCase("date")){return "java.sql.Date";}
        else if(columnType.equalsIgnoreCase("time")){return "java.sql.Time";}
        else if(columnType.equalsIgnoreCase("timestamp")){return "java.sql.Timestamp";}
            return null;
    }

    @Override
    public String javaType2databaseType(String javaType) {
        return null;
    }
}
