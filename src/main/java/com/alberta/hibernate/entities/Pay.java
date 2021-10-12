package com.alberta.hibernate.entities;

public class Pay {
    private Integer monthlyPay;
    private Integer yearPay;
    private Integer VocationWithPay;

    private Worker worker;

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    @Override
    public String toString() {
        return "Pay{" +
                "monthlyPay=" + monthlyPay +
                ", yearPay=" + yearPay +
                ", VocationWithPay=" + VocationWithPay +
                '}';
    }

    public Pay() {
    }

    public Pay(Integer monthlyPay, Integer yearPay, Integer vocationWithPay) {
        this.monthlyPay = monthlyPay;
        this.yearPay = yearPay;
        VocationWithPay = vocationWithPay;
    }

    public Integer getMonthlyPay() {
        return monthlyPay;
    }

    public void setMonthlyPay(Integer monthlyPay) {
        this.monthlyPay = monthlyPay;
    }

    public Integer getYearPay() {
        return yearPay;
    }

    public void setYearPay(Integer yearPay) {
        this.yearPay = yearPay;
    }

    public Integer getVocationWithPay() {
        return VocationWithPay;
    }

    public void setVocationWithPay(Integer vocationWithPay) {
        VocationWithPay = vocationWithPay;
    }
}
