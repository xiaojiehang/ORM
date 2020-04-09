package cn.xjh.sorm.bean;
/*封装表中一个字段的信息*/
public class ColumnInfo {
    private String name;//字段名称

    private String dataType;//字段数据类型

    private int KeyType;//0代表非主键，1代表主键，2代表外键

    public ColumnInfo() {
    }

    public ColumnInfo(String name, String dataType, int keyType) {
        this.name = name;
        this.dataType = dataType;
        KeyType = keyType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getKeyType() {
        return KeyType;
    }

    public void setKeyType(int keyType) {
        KeyType = keyType;
    }
}
