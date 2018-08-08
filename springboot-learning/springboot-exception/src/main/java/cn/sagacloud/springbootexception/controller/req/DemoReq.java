package cn.sagacloud.springbootexception.controller.req;

import cn.sagacloud.springbootexception.valid.Constant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemoReq {
    @NotBlank(message = "code不能为空")
    String code;
String name;
    @Constant(message = "version只能为1.0",value = "1.0")
    String version;
}
