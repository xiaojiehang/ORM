package cn.xjh.sorm.bean;

public class Configuration {
    private String driver;//数据库驱动
    private String url;//数据库url
    private String user;//数据库用户名
    private String password;//数据库密码
    private String src;//项目源码路径
    private String popackage;//扫描后生成java类存储的包，po：Persistence object持久化对象
    private String usingdb;//选择数据库类型
    private String queryClass;//项目选择的查询类，即Query的一个子类

    public Configuration() {//无参构造
    }


    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getPopackage() {
        return popackage;
    }

    public void setPopackage(String popackage) {
        this.popackage = popackage;
    }

    public String getUsingdb() {
        return usingdb;
    }

    public void setUsingdb(String usingdb) {
        this.usingdb = usingdb;
    }

    public String getQueryClass() {
        return queryClass;
    }

    public void setQueryClass(String queryClass) {
        this.queryClass = queryClass;
    }
}
