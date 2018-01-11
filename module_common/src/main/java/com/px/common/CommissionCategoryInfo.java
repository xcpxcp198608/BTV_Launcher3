package com.px.common;

import java.math.BigDecimal;

public class  CommissionCategoryInfo {

    private int id;
    private String category;
    private float deposit;
    private int expires;
    private int bonus;
    private float ldCommission;
    private float dealerCommission;
    private float salesCommission;

    private float price;
    private float firstPay;
    private float monthPay;

    public float getPrice() {
        return price;
    }

    public void setPrice() {
        BigDecimal b = new BigDecimal(this.monthPay * this.expires + this.deposit);
        this.price = b.setScale(2,  BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public float getFirstPay() {
        return firstPay;
    }

    public void setFirstPay() {
        BigDecimal b = new BigDecimal(this.monthPay + this.deposit);
        this.firstPay = b.setScale(2,  BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public float getMonthPay() {
        return monthPay;
    }

    public void setMonthPay(float monthPay) {
        this.monthPay = monthPay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getDeposit() {
        return deposit;
    }

    public void setDeposit(float deposit) {
        this.deposit = deposit;
    }

    public int getExpires() {
        return expires;
    }

    public void setExpires(int expires) {
        this.expires = expires;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public float getLdCommission() {
        return ldCommission;
    }

    public void setLdCommission(float ldCommission) {
        this.ldCommission = ldCommission;
    }

    public float getDealerCommission() {
        return dealerCommission;
    }

    public void setDealerCommission(float dealerCommission) {
        this.dealerCommission = dealerCommission;
    }

    public float getSalesCommission() {
        return salesCommission;
    }

    public void setSalesCommission(float salesCommission) {
        this.salesCommission = salesCommission;
    }

    @Override
    public String toString() {
        return "CommissionCategoryInfo{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", firstPay=" + firstPay +
                ", monthPay=" + monthPay +
                ", deposit=" + deposit +
                ", expires=" + expires +
                ", bonus=" + bonus +
                ", ldCommission=" + ldCommission +
                ", dealerCommission=" + dealerCommission +
                ", salesCommission=" + salesCommission +
                '}';
    }
}
