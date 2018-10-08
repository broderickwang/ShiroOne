package com.ttb.controller;

import com.ttb.dao.UserDao;
import com.ttb.domain.UserDO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @project: ShiroOne
 * @author: wangchengda
 * @email: wangchengda1990@gmail.com
 * @date: 2018/10/8
 * @Description:
 * @Copyright: 2018 broderickwang.github.io Inc. All rights reserved.
 * @version: V1.0
 */
@Controller
@RequestMapping("")
public class IndexController {

    @Autowired
    UserDao userDao;

    @GetMapping("/one")
    @ResponseBody
    public String one(/*HttpServletRequest req, HttpServletResponse rsp*/) throws Exception{
//        PrintWriter out = rsp.getWriter();
        UserDO userDO = userDao.getOne("12");
//        out.write(userDO.toString());
        return userDO.toString();
    }

    @GetMapping("/login")
    public String index(){
        return "/login";
    }

    @PostMapping("/subLogin")
    @ResponseBody
    public String subLogin( UserDO userDO){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userDO.getUsername(),userDO.getPassword());
        try {
            subject.login(token);
        }catch (Exception e){
            return e.getLocalizedMessage();
        }
        if (subject.hasRole("admin")){
            return "have admin permission!";
        }
        return "no admin permission!";
    }

    @GetMapping("/1")
    public String index1(){
        return "/hello1";
    }

    @GetMapping("/403")
    public String un403(){
        return "/403";
    }
}
