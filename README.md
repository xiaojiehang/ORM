# ORM
一个简单的ORM框架

## 该orm支持的功能

调用简单函数实现对指定数据库的增删查改  
将自定义对象生成数据库的字段  
将数据库表的字段生成java bean对象  

## 使用方法：  
创建一个demo项目，引入jar包  
将配置文件放入src目录下  
调用TableContext.loadPoTables()方法自动按数据库的表构建java类  

## 项目结构：  
核心类：  
Query抽象类:负责查询（对外提供服务核心类）  
QueryFactory类：负责根据配置信息创建query对象  
TypeConverto类:负责java与数据库的数据类型转换  
TableContext类:负责获取管理数据库所有表结构和类结构的关系，并可以根据表结构生成类结构（调用工具类）  
DBManger:根据配置信息，维持连接对象的管理（增加连接池）  
工具类：  
JDBCUtils封装常用JDBC操作  
StringUtils封装常用字符串操作  
JavaFileUtils封装文件操作：（将表结构封装成java类的操作）  
ReflectUtils封装常用反射操作    
bean类:  
ColumnInfo类：封装数据库表中一个字段的信息  
Configuration类：封装配置信息类  
TableInfo类：封装数据库表的信息（表结构+表内所有的字段）

### 配置文件格式：
driver：数据库驱动器  
url：连接数据库的url，最后一个sorm是数据库的名字  
user：数据库用户名  
password：密码  
src：当前项目的src目录  
popackage：数据库表生成的java对象存储的包  
usingdb：使用的数据库类型  
queryClass：使用的Query类（目前只有MysqlQuery类，只支持mysql）

[CSDN博客](https://blog.csdn.net/Hsiao99/article/details/105375106)
