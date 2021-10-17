package com.alberta.hibernate.entities.one2one;

public class Manager {
    private Integer managerId;
    private String managerName;
    private Department department;

    public Manager() {
    }

    public Manager(Integer managerId, String managerName, Department department) {
        this.managerId = managerId;
        this.managerName = managerName;
        this.department = department;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "managerId=" + managerId +
                ", managerName='" + managerName + '\'' +
                ", department=" + department +
                '}';
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
