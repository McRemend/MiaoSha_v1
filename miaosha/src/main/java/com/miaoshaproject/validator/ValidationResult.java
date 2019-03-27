package com.miaoshaproject.validator;

import java.util.HashMap;
import java.util.Map;

public class ValidationResult {
    //校验是否有错
    private boolean hasError=false;
    //存放错误信息的map
    private Map<String,String> errorMsgMap = new HashMap<>();

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public Map<String, String> getErrorMsgMap() {
        return errorMsgMap;
    }

    public void setErrorMsgMap(Map<String, String> errorMsgMap) {
        this.errorMsgMap = errorMsgMap;
    }

    //实现通用的msg方法
    public String getErrorMsg(){
        return org.apache.commons.lang3.StringUtils.join(errorMsgMap.values().toArray(),",");
    }
}
