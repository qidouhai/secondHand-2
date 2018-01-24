package cn.chenny3.secondHand.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

public abstract class BaseController {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());


    public String objectErrorsToString(BindingResult bindingResult) {

        StringBuilder stringBuilder = new StringBuilder();
        for (ObjectError objectError : bindingResult.getAllErrors()) {
            String fieldName = objectError.getObjectName();
            String errorMsg = objectError.getDefaultMessage();
            stringBuilder.append(fieldName).append(":").append(errorMsg).append(";");
        }

        return stringBuilder.toString();

    }
}
