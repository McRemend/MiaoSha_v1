package com.miaoshaproject.service;


import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.ItemModel;

import java.util.List;

public interface ItemService {
    //创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    //商品列表浏览
    List<ItemModel> listItem();

    //商品详情浏览
    ItemModel getItemInfo(Integer id);

    //落单减库存
    boolean decreaseStock(Integer itemId,Integer amount) throws  BusinessException;

    //销量增加
    void increaseSales(Integer itemId,Integer amount) throws BusinessException;


}
