package cn.sagacloud.springbootexception.controller;

import cn.sagacloud.springbootexception.controller.req.DemoReq;
import cn.sagacloud.springbootexception.exception.CommonException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class DemoController {
    @GetMapping("/demo")
    public String demo(){
        throw new CommonException("01001","发送异常");
    }

    @GetMapping("/demo/commonValid")
    public String commonValid(@Valid DemoReq req){
        return req.getCode() + "," + req.getName();
    }
}
