package cn.sagacloud.springbootexception.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author SAGACLOUD
 */
public class ConstantValidatorHandler implements ConstraintValidator<Constant,String> {

    private String constant;

    @Override
    public void initialize(Constant constraintAnnotation){
        //获取设置的字段
        this.constant=constraintAnnotation.value();
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        //判断参数是否等于设置的字段值，返回结果
        return constant.equals(value);
    }
}
