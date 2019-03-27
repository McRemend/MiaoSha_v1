package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.OrderModel;

public interface OrderService {
    //用户下单,通过前端上传过来的活动id，然后下单接口内校验对应的id是否属于对应的商品且活动一开始
    //直接在下单接口内判断对应的商品是否存在秒杀活动，若存在则以秒杀活动下单
    //优选第一种，可扩展，一个商品含有多个秒杀活动的话。
    OrderModel createOrder(Integer userId,Integer itemId,Integer amount,Integer promoId) throws BusinessException;

}
