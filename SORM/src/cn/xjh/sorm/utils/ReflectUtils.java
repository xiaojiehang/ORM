package cn.xjh.sorm.utils;
import java.lang.reflect.Method;
/*封装反射的的常用操作*/
public class ReflectUtils {

    /*调用obj对象的某一属性的get方法*/
    public static  Object invokeGet(String fieldName,Object obj){
        //通过反射机制，调用属性的get或set方法
        try {
            Class c=obj.getClass();
            Method m=c.getDeclaredMethod("get"+StringUtils.firstChar2UpperCase(fieldName),null);
            return m.invoke(obj,null);//调用get方法得到该属性的值
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void invokeSet(Object obj,String fieldName,Object fieldValue){
        //通过反射机制，调用属性的get或set方法
        if(fieldValue!=null){
        try {
            Class c=obj.getClass();
            Method m=c.getDeclaredMethod("set"+StringUtils.firstChar2UpperCase(fieldName),fieldValue.getClass());
            m.invoke(obj,fieldValue);//调用get方法得到该属性的值
        } catch (Exception e) {
            e.printStackTrace();
        }}
    }
}
