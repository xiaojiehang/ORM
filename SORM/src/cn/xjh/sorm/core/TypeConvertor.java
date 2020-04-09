package cn.xjh.sorm.core;
/*类型转换器接口：将java数据类型与数据库数据类型相互转换*/
public interface TypeConvertor {

   /*将数据库数据类型转换为java类型
   *columnType：数据库字段的数据类型
   * return java的数据类型 */
    public String databaseType2javaType(String columnType);

    /*将java数据类型转换为数据库数据类型
     *javaType：java内的数据类型
     * return 数据库的数据类型 */
    public String javaType2databaseType(String javaType);
}
