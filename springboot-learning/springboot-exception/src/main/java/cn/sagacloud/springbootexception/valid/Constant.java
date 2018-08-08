package cn.sagacloud.springbootexception.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
/**
 * 自定义校验注解
 * 主要时实现ConstraintValidator的处理类即可，这里已编写一个校验常量的注解为
 * 例：参数值只能为特定的值
 */
@Documented
//指定注解的处理类
@Constraint(validatedBy = {ConstantValidatorHandler.class})
@Target({METHOD,FIELD,ANNOTATION_TYPE,CONSTRUCTOR,PARAMETER})
@Retention(RUNTIME)
public @interface Constant {

    String message() default "{constraint.default.const.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String value();
}
