package com.miaoshaproject.service;

import com.miaoshaproject.service.model.PromoModel;

public interface PromoService {
    //根据itemid获取即将进行的秒杀活动时间
    PromoModel getPromoByItemId(Integer itemId);
}
