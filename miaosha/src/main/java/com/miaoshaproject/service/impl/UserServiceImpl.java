package com.miaoshaproject.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.miaoshaproject.dao.UserDOMapper;
import com.miaoshaproject.dao.UserPasswordDOMapper;
import com.miaoshaproject.dataobject.UserDO;
import com.miaoshaproject.dataobject.UserPasswordDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import com.miaoshaproject.validator.ValidationResult;
import com.miaoshaproject.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private  UserPasswordDOMapper userPasswordDOMapper;

    @Override
    public UserModel getUserById(Integer id){
    //        返回用户对象
       UserDO userDO = userDOMapper.selectByPrimaryKey(id);
       if (userDO!=null){
           UserPasswordDO userPasswordDO= userPasswordDOMapper.selectByUserId(userDO.getId());
           UserModel userModel = converFromDataProject(userDO,userPasswordDO);
           return userModel;
       }else {
           return  null;
       }
    }

    @Autowired
    private ValidatorImpl validator;

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if (userModel==null){
            throw  new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
       /* if(StringUtils.isEmpty(userModel.getName())
                ||userModel.getGender()==null
                ||userModel.getAge()==null
                ||StringUtils.isEmpty(userModel.getTelphone())){
          throw new  BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }*/
        ValidationResult result = validator.validate(userModel);
        if (result.isHasError()){
            throw new  BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrorMsg());
        }

        //实现model>dataobject方法
        UserDO userDO = convertFromModel(userModel);
        userDOMapper.insertSelective(userDO);
        userModel.setId(userDO.getId());
        UserPasswordDO userPasswordDO = converPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);
    }

    @Override
    public UserModel vaildateLogin(String telphone, String password) throws BusinessException {
        //通过用户的手机获取用户信息
        UserDO userDO= userDOMapper.selectByTelphone(telphone);
        if (userDO==null){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        //比对用户信息内加密的密码是否和传输的密码一致
        //通过返回的用户id，查询密码表里面的用户密码比对
        UserPasswordDO userPasswordDO =userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = converFromDataProject(userDO,userPasswordDO);
        if (!StringUtils.equals(password,userModel.getEncrptPassword())){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }

    private UserPasswordDO converPasswordFromModel(UserModel userModel){
        if (userModel == null){
            return  null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }
    private UserDO convertFromModel(UserModel userModel){
        if (userModel == null){
            return  null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;

    }

    private UserModel converFromDataProject(UserDO userDO, UserPasswordDO userPasswordDO){
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO,userModel);
        if (userPasswordDO!=null){
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }

        return  userModel;
    }
}
