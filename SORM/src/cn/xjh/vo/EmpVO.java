package cn.xjh.vo;

public class EmpVO {//联合查询
    //例如select e.id,e.empname ,e.salary,d.dname from emp e JOIN dept d on e.deptID=d.id
    //专门建一个java bean来存放查询结果
    private Integer id;
    private String empname;
    private Double salary;
    private String dname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }


    public EmpVO() {
    }
}
