package com.miaoshaproject.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Set;


@Component
public class ValidatorImpl implements InitializingBean {

    @Autowired
    private Validator validator;

    //实现校验方法并返回校验结果
    public ValidationResult validate(Object bean){
        ValidationResult validationResult=new ValidationResult();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(bean, Default.class);
        if (constraintViolations.size()>0){
            //有错误
            validationResult.setHasError(true);
            constraintViolations.forEach(constraintViolation->{
                String errMsg=constraintViolation.getMessage();
                String propertName = constraintViolation.getPropertyPath().toString();
                validationResult.getErrorMsgMap().put(propertName,errMsg);
            });
        }
        return validationResult;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //将hibernate validator 通过工程的初始化方式使其实例化
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
