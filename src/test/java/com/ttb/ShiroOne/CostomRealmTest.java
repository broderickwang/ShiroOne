package com.ttb.ShiroOne;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.security.auth.Subject;

/**
 * @project: ShiroOne
 * @author: wangchengda
 * @email: wangchengda1990@gmail.com
 * @date: 2018/9/18
 * @Description:
 * @Copyright: 2018 broderickwang.github.io Inc. All rights reserved.
 * @version: V1.0
 */
@SpringBootTest
public class CostomRealmTest {
    @Test
    public void excute(){

        CustomRealm customRealm = new CustomRealm();

        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm);

        SecurityUtils.setSecurityManager(defaultSecurityManager);
        org.apache.shiro.subject.Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("LanCoder","123456");
        subject.login(usernamePasswordToken);

        System.out.println("is authored :"+subject.isAuthenticated());

        subject.checkRoles("admin","teacher");
        subject.checkPermission("sys:add:user");
    }
}
