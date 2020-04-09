package cn.xjh.sorm.core;
/*根据配置文件创建Query对象
*工厂类是单例模式
* 使用原型模式，工厂模式 ，通过该类获得需要的Query对象*/
public class QueryFactory {
    private static Query prototypeObj;
    static {//静态加载配置文件的指定的Query类
        try {
            Class clazz=Class.forName(DBManger.getConf().getQueryClass());
            prototypeObj= (Query) clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  private QueryFactory(){};
  public static Query createQuery(){
      try {
          return (Query) prototypeObj.clone();
      } catch (Exception e) {
          e.printStackTrace();
          return null;
      }
  };
}
