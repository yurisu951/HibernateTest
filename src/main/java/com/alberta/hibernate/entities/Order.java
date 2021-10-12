package com.alberta.hibernate.entities;

public class Order {
    private Integer orderId;
    private String orderName;

    private Customer customer;


    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderName='" + orderName + '\'' +
                ", customer=" + customer +
                '}';
    }

    public Order() {
    }

    public Order(Integer orderId, String orderName, Customer customer) {
        this.orderId = orderId;
        this.orderName = orderName;
        this.customer = customer;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
