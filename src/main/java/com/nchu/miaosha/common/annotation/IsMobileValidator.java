package com.nchu.miaosha.common.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.nchu.miaosha.common.utils.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName: IsMobileValidator
 * @Author: 时间
 * @Description: 手机格式校验实现
 * @Date: 2020/7/14 22:10
 * @Version: 1.0
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    private boolean required = false;

    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (required) { // 不允许为空
            return ValidatorUtil.isMobile(value);
        } else {
            if (StringUtils.isEmpty(value)) {
                return true;
            } else {
                return ValidatorUtil.isMobile(value);
            }
        }
    }

}
