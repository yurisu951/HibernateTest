package com.alberta.hibernate.entities.both;

import java.util.HashSet;
import java.util.Set;

public class Customer {
    private Integer customerId;
    private String customerName;

//    1。聲明集合類型時，需使用集合類型，因為hibernate在獲取集合類型時，返回的是Hibernate內置的集合類型
//    2。需要把集合進行初始化，可以防止發生空指針異常
    private Set Orders = new HashSet();

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                '}';
    }

    public Set getOrders() {
        return Orders;
    }

    public void setOrders(Set orders) {
        Orders = orders;
    }

    public Customer() {
    }

    public Customer(Integer customerId, String customerName) {
        this.customerId = customerId;
        this.customerName = customerName;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
