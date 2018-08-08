package cn.sagacloud.springbootexception.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 自定义异常类
 * @author SAGACLOUD
 */
@Data
public class CommonException extends RuntimeException {

    private static final long serialVersionUID = 7438627252255549940L;

    String code;
    String msg;
    public CommonException(String code,String msg){
        super(code+msg);
        this.code=code;
        this.msg=msg;
    }
}
