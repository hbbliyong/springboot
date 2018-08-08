## 统一异常处理、数据校验
### 1.统一异常处理
显然，默认的异常页是对用户或者调用者而言都是不友好的，所以一般上我们都会进行实现自己业务的异常提示信息。

创建全局的统一异常处理类
利用@ControllerAdvice和@ExceptionHandler定义一个统一异常处理类

@ControllerAdvice：控制器增强，使@ExceptionHandler、@InitBinder、@ModelAttribute注解的方法应用到所有的 @RequestMapping注解的方法。
@ExceptionHandler：异常处理器，此注解的作用是当出现其定义的异常时进行处理的方法
创建异常类：CommonExceptionHandler

```java


@ControllerAdvice
public class CommonExceptionHandler {
 
    /**
     *  拦截Exception类的异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Map<String,Object> exceptionHandler(Exception e){
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("respCode", "9999");
        result.put("respMsg", e.getMessage());
        //正常开发中，可创建一个统一响应实体，如CommonResp
        return result; 
    }
}
```
多余不同异常(如自定义异常)，需要进行不同的异常处理时，可编写多个exceptionHandler方法，注解ExceptionHandler指定处理的异常类，如

```java


/**
 * 拦截 CommonException 的异常
 * @param ex
 * @return
 */
@ExceptionHandler(CommonException.class)
@ResponseBody
public Map<String,Object> exceptionHandler(CommonException ex){
    log.info("CommonException：{}({})",ex.getMsg(), ex.getCode());
    Map<String,Object> result = new HashMap<String,Object>();
    result.put("respCode", ex.getCode());
    result.put("respMsg", ex.getMsg());
    return result; 
}
```
由于加入了@ResponseBody，所以返回的是json格式，



说明异常已经被拦截了。

可拦截不同的异常，进行不同的异常提示，比如NoHandlerFoundException、HttpMediaTypeNotSupportedException、AsyncRequestTimeoutException等等，这里就不列举了，读者可自己加入后实际操作下。

对于返回页面时，返回ModelAndView即可，如

```java
@ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }
```    
由于工作中都是才有前后端分离开发模式，所以一般上都没有直接返回资源页的需求了，一般上都是返回固定的响应格式，如respCode、respMsg、data，前端通过判断respCode的值进行业务判断，是弹窗还是跳转页面。
### 2.数据校验
数据校验
在web开发时，对于请求参数，一般上都需要进行参数合法性校验的，原先的写法时一个个字段一个个去判断，这种方式太不通用了，所以java的JSR 303: Bean Validation规范就是解决这个问题的。

JSR 303只是个规范，并没有具体的实现，目前通常都是才有hibernate-validator进行统一参数校验。

JSR303定义的校验类型

Constraint	详细信息
@Null	被注释的元素必须为 null
@NotNull	被注释的元素必须不为 null
@AssertTrue	被注释的元素必须为 true
@AssertFalse	被注释的元素必须为 false
@Min(value)	被注释的元素必须是一个数字，其值必须大于等于指定的最小值
@Max(value)	被注释的元素必须是一个数字，其值必须小于等于指定的最大值
@DecimalMin(value)	被注释的元素必须是一个数字，其值必须大于等于指定的最小值
@DecimalMax(value)	被注释的元素必须是一个数字，其值必须小于等于指定的最大值
@Size(max, min)	被注释的元素的大小必须在指定的范围内
@Digits (integer, fraction)	被注释的元素必须是一个数字，其值必须在可接受的范围内
@Past	被注释的元素必须是一个过去的日期
@Future	被注释的元素必须是一个将来的日期
@Pattern(value)	被注释的元素必须符合指定的正则表达式
Hibernate Validator 附加的 constraint

Constraint	详细信息
@Email	被注释的元素必须是电子邮箱地址
@Length	被注释的字符串的大小必须在指定的范围内
@NotEmpty	被注释的字符串的必须非空
@Range	被注释的元素必须在合适的范围内
创建实体类

```java

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemoReq {
     
    @NotBlank(message="code不能为空")
    String code;
     
    @Length(max=10,message="name长度不能超过10")
    String name;
 
}

```
然后在控制层方法里，加入@Valid即可，这样在访问前，会对请求参数进行检验。
```java


@GetMapping("/demo/valid")
public String demoValid(@Valid DemoReq req) {
    return req.getCode() + "," + req.getName();
}
```
启动，后访问http://127.0.0.1:8080/demo/valid



加上正确参数后，http://127.0.0.1:8080/demo/valid?code=3&name=s



这样数据的统一校验就完成了，对于其他注解的使用，大家可自行谷歌下，基本上都很简单的，对于已有的注解无法满足校验需要时，也可进行自定义注解的开发，一下简单讲解下，自定义注解的编写

不使用@valid的情况下，也可利用编程的方式编写一个工具类，进行实体参数校验

```java


public class ValidatorUtil {
    private static Validator validator = ((HibernateValidatorConfiguration) Validation
            .byProvider(HibernateValidator.class).configure()).failFast(true).buildValidatorFactory().getValidator();
 
    /**
     * 实体校验
     * 
     * @param obj
     * @throws CommonException
     */
    public static <T> void validate(T obj) throws CommonException {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj, new Class[0]);
        if (constraintViolations.size() > 0) {
            ConstraintViolation<T> validateInfo = (ConstraintViolation<T>) constraintViolations.iterator().next();
            // validateInfo.getMessage() 校验不通过时的信息，即message对应的值
            throw new CommonException("0001", validateInfo.getMessage());
        }
    }
}
```
使用

```
@GetMapping("/demo/valid")
public String demoValid(@Valid DemoReq req) {
    //手动校验
    ValidatorUtil.validate(req);
    return req.getCode() + "," + req.getName();
}
```
自定义校验注解
自定义注解，主要时实现ConstraintValidator的处理类即可，这里已编写一个校验常量的注解为例：参数值只能为特定的值。

自定义注解

```java


@Documented
//指定注解的处理类
@Constraint(validatedBy = {ConstantValidatorHandler.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface Constant {
 
   String message() default "{constraint.default.const.message}";
 
   Class<?>[] groups() default {};
 
   Class<? extends Payload>[] payload() default {};
 
   String value();
 
}
```
注解处理类

```java


public class ConstantValidatorHandler implements ConstraintValidator<Constant, String> {
 
    private String constant;
 
    @Override
    public void initialize(Constant constraintAnnotation) {
        //获取设置的字段值
        this.constant = constraintAnnotation.value();
    }
 
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //判断参数是否等于设置的字段值，返回结果
        return constant.equals(value);
    }
 
}
```
使用
```
@Constant(message = "verson只能为1.0",value="1.0")
String version;

```

此时，自定义注解已生效。大家可根据实际需求进行开发。

大家看到在校验不通过时，返回的异常信息是不友好的，此时可利用统一异常处理，对校验异常进行特殊处理，特别说明下，对于异常处理类,共有以下几种情况(被@RequestBody和@RequestParam注解的请求实体，校验异常类是不同的)
```java
@ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,Object> handleBindException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        log.info("参数校验异常:{}({})", fieldError.getDefaultMessage(),fieldError.getField());
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("respCode", "01002");
        result.put("respMsg", fieldError.getDefaultMessage());
        return result;
    }
 
 
    @ExceptionHandler(BindException.class)
    public Map<String,Object> handleBindException(BindException ex) {
        //校验 除了 requestbody 注解方式的参数校验 对应的 bindingresult 为 BeanPropertyBindingResult
        FieldError fieldError = ex.getBindingResult().getFieldError();
        log.info("必填校验异常:{}({})", fieldError.getDefaultMessage(),fieldError.getField());
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("respCode", "01002");
        result.put("respMsg", fieldError.getDefaultMessage());
        return result;
    }
```    
启动后，提示就友好了



所以统一异常还是很有必要的。

总结
本章节主要是阐述了统一异常处理和数据的合法性校验，同时简单实现了一个自定义的注解类，大家在碰见已有注解无法解决时，可通过自定义的形式进行，当然对于通用而已，利用@Pattern(正则表达式)基本上都是可以实现的。

最后