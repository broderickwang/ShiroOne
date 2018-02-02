package com.ttb.ShiroOne;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version: 1.0.0
 * @author: wangcd
 * @date: 一月 31 2018,14:29
 * @description:
 **/

@Controller
public class One {

    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }
}
