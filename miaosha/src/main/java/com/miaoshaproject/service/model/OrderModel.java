package com.miaoshaproject.service.model;

import java.math.BigDecimal;

//用户下单交易模型
public class OrderModel {
    private String id;//订单id
    private Integer userId;//购买的用户id
    private Integer itemId;//购买的商品ID
    private Integer amount;//购买商品的数量
    private BigDecimal orderprice;//购买的商品总价
    private BigDecimal itemPrice;//商品购买的当时的价格
    private Integer promoId;//若promoId非空，则以秒杀商品方式下单

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public BigDecimal getOrderprice() {
        return orderprice;
    }

    public void setOrderprice(BigDecimal orderprice) {
        this.orderprice = orderprice;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
