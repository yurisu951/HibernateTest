package com.alberta.hibernate.entities.one2one;

public class Department {
    private Integer departmentId;
    private String departmentName;
    private Manager manager;


    public Department() {
    }

    public Department(Integer departmentId, String departmentName, Manager manager) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.manager = manager;
    }

    @Override
    public String toString() {
        return "Department{" +
                "departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                ", manager=" + manager +
                '}';
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
}
