package cn.xjh.sorm.bean;
/*封装java属性和get，set方法源代码*/
public class JavaFieldGetSet {
    private String fieldInfo;
    private String getInfo;
    private String setInfo;

    public String getFieldInfo() {
        return fieldInfo;
    }

    public void setFieldInfo(String fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    public String getGetInfo() {
        return getInfo;
    }

    public void setGetInfo(String getInfo) {
        this.getInfo = getInfo;
    }

    public String getSetInfo() {
        return setInfo;
    }

    public void setSetInfo(String setInfo) {
        this.setInfo = setInfo;
    }

    @Override
    public String toString() {
        System.out.println(this.getFieldInfo());
        System.out.println(this.getGetInfo());
        System.out.println(this.getSetInfo());
        return super.toString();
    }
}
