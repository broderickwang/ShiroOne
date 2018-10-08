package com.ttb.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @project: ShiroOne
 * @author: wangchengda
 * @email: wangchengda1990@gmail.com
 * @date: 2018/10/8
 * @Description:
 * @Copyright: 2018 broderickwang.github.io Inc. All rights reserved.
 * @version: V1.0
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @RequiresRoles("admin")
    @GetMapping("/role")
    public String test(){
        return "test role";
    }

    @RequiresRoles("xxxx")
    @GetMapping("/role1")
    public String test1(){
        return "test role1";
    }
}
