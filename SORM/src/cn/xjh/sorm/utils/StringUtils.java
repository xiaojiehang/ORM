package cn.xjh.sorm.utils;
/*封装对字符串的常用处理操作*/
public class StringUtils {
    /*将目标字符首字母变为大写
    * str为目标字符串
    * return 首字母变为大小的字符串*/
    public static String firstChar2UpperCase(String str){
            return  str.toUpperCase().substring(0,1)+str.substring(1);//例如：将str全部转为大写，截取第一个字符，再拼接原str的剩余字符
    }
}
