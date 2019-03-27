package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.ItemDOMapper;
import com.miaoshaproject.dao.ItemStockDOMapper;
import com.miaoshaproject.dataobject.ItemDO;
import com.miaoshaproject.dataobject.ItemStockDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.PromoService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.service.model.PromoModel;
import com.miaoshaproject.validator.ValidationResult;
import com.miaoshaproject.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemDOMapper itemDOMapper;

    @Autowired
    private ItemStockDOMapper itemStockDOMapper;

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private PromoService promoService;

    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        ValidationResult result = validator.validate(itemModel);
        if (result.isHasError()){
            throw new  BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrorMsg());
        }
        //转化
        ItemDO itemDO=coverItemDOFromModel(itemModel);
        //插入数据库
        itemDOMapper.insertSelective(itemDO);
        itemModel.setId(itemDO.getId());

        ItemStockDO itemStockDO=this.coverItemStockDOFromModel(itemModel);
        itemStockDO.setItemId(itemModel.getId());
        itemStockDOMapper.insertSelective(itemStockDO);

        //返回创建完的对象
        return this.getItemInfo(itemModel.getId());
    }

    private ItemDO coverItemDOFromModel(ItemModel itemModel){
        if (itemModel==null){
           return  null;
        }
        ItemDO itemDO=new ItemDO();
        BeanUtils.copyProperties(itemModel,itemDO);
        itemDO.setDesciption(itemModel.getDescription());
        itemDO.setImgurl(itemModel.getImgUrl());
        return itemDO;
    }

    private ItemStockDO coverItemStockDOFromModel(ItemModel itemModel){
        if (itemModel==null){
            return  null;
        }
        ItemStockDO itemStockDO=new ItemStockDO();
        BeanUtils.copyProperties(itemModel,itemStockDO);
        /*itemDO.setPrice(itemModel.getPrice().doubleValue());*/
        return itemStockDO;
    }


    @Override
    public List<ItemModel> listItem() {
        List<ItemDO> itemDOList = itemDOMapper.listItem();
        List<ItemModel> itemModelList=itemDOList.stream().map(itemDO -> {
            ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
            ItemModel itemModel=this.converItemModelFromItemDO(itemDO,itemStockDO);
            return itemModel;
        }).collect(Collectors.toList());
        return itemModelList;
    }

    @Override
    public ItemModel getItemInfo(Integer id) {
        ItemDO itemDO=itemDOMapper.selectByPrimaryKey(id);
        if (itemDO==null){
            return null;
        }
        //操作获得库存数量
        ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
        //将dataobeject->model
        ItemModel itemModel=converItemModelFromItemDO(itemDO,itemStockDO);

        //获取活动商品信息
        PromoModel promoModel=promoService.getPromoByItemId(itemModel.getId());
        if (promoModel!=null && promoModel.getStatus().intValue()!=3){
            itemModel.setPromoModel(promoModel);
        }
        return itemModel;
    }

    @Override
    public boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException {
        int affectedRow =itemStockDOMapper.decreaseStock(itemId,amount);
        if (affectedRow>0){
            //更新库存成功
            return true;
        }else{
            //更新库存失败
            return false;
        }
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId,Integer amount) throws BusinessException {
        itemDOMapper.increaseSales(itemId,amount);
    }

    private ItemModel converItemModelFromItemDO(ItemDO itemDO,ItemStockDO itemStockDO){
        ItemModel itemModel =new ItemModel();
        BeanUtils.copyProperties(itemDO,itemModel);
        itemModel.setImgUrl(itemDO.getImgurl());
        itemModel.setDescription(itemDO.getDesciption());
        itemModel.setStock(itemStockDO.getStock());
        return itemModel;
    }
}
