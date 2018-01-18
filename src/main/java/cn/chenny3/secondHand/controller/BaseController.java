package cn.chenny3.secondHand.controller;

import cn.chenny3.secondHand.model.Category;
import cn.chenny3.secondHand.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

public abstract class BaseController {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected CategoryService categoryService;

    public String objectErrorsToString(BindingResult bindingResult) {

        StringBuilder stringBuilder = new StringBuilder();
        for (ObjectError objectError : bindingResult.getAllErrors()) {
            String fieldName = objectError.getObjectName();
            String errorMsg = objectError.getDefaultMessage();
            stringBuilder.append(fieldName).append(":").append(errorMsg).append(";");
        }

        return stringBuilder.toString();

    }

    public List<Category> getNavCategories(){
        return categoryService.selectCategoriesByParentId(0);
    }

}
