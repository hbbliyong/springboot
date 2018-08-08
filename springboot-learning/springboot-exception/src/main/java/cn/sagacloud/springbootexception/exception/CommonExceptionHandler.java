package cn.sagacloud.springbootexception.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建全局的统一异常处理类
 利用@ControllerAdvice和@ExceptionHandler定义一个统一异常处理类

 *@ControllerAdvice：控制器增强，使@ExceptionHandler、@InitBinder、@ModelAttribute注解的方法应用到所有的 @RequestMapping注解的方法。
 *@ExceptionHandler：异常处理器，此注解的作用是当出现其定义的异常时进行处理的方法
 */
@ControllerAdvice
@Slf4j
public class CommonExceptionHandler {
    /**
     * 拦截Exception类的异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Map<String,Object> exceptionHandler(Exception e){
        Map<String,Object> result=new HashMap<>();
        result.put("respCode","9999");
        result.put("respMsg",e.getMessage());
        //正常开发中，可创建一个统一响应实体，如CommonResp
        return result;
    }

   // 多余不同异常(如自定义异常)，需要进行不同的异常处理时，可编写多个exceptionHandler方法，注解ExceptionHandler指定处理的异常类

    /**
     * 拦截CommonException的异常
     * @param ex
     * @return
     */
    @ExceptionHandler(CommonException.class)
    @ResponseBody
    public Map<String,Object> exceptionHandler(CommonException ex){
        log.info("CommonException：{}({})",ex.getMsg(), ex.getCode());
        Map<String,Object> result=new HashMap<>();
        result.put("respCode",ex.getCode());
        result.put("respMsg",ex.getMsg());
        return result;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Map<String,Object> handleBindException(MethodArgumentNotValidException ex){
        FieldError fieldError=ex.getBindingResult().getFieldError();
        log.info("参数校验异常:{}({})",fieldError.getDefaultMessage(),fieldError.getField());
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("respCode", "01002");
        result.put("respMsg", fieldError.getDefaultMessage());
        return result;
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Map<String,Object> handleBindException(BindException ex) {
        //校验 除了 requestbody 注解方式的参数校验 对应的 bindingresult 为 BeanPropertyBindingResult
        FieldError fieldError = ex.getBindingResult().getFieldError();
        log.info("必填校验异常:{}({})", fieldError.getDefaultMessage(),fieldError.getField());
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("respCode", "01002");
        result.put("respMsg", fieldError.getDefaultMessage());
        return result;
    }
}
