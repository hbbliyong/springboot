package cn.smiler.springbootshiro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class HomeController {
    @RequestMapping({"/","/index"})
    public String index(){
        return "/index";
    }

@RequestMapping("/login")
public String login(HttpServletRequest request,Map<String,Object> map) throws Exception {
    System.out.println("HomeController.login()");
    // 此方法不处理登录成功,由shiro进行处理
    return "/login";
}
    @RequestMapping("403")
    public String unauthorizedRole(){
        System.out.println("------没有权限-------");
        return "403";
    }
}
