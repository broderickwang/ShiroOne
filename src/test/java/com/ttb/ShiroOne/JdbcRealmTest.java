package com.ttb.ShiroOne;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.DatabaseMetaData;

/**
 * @project: ShiroOne
 * @author: wangchengda
 * @email: wangchengda1990@gmail.com
 * @date: 2018/9/16
 * @Description:
 * @Copyright: 2018 broderickwang.github.io Inc. All rights reserved.
 * @version: V1.0
 */
@SpringBootTest
public class JdbcRealmTest {

    DruidDataSource druidDataSource = new DruidDataSource();
    {
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/test");

        druidDataSource.setUsername("root");
        druidDataSource.setPassword("root");
    }

    @Test
    public void excute(){

        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(druidDataSource);
        //设置权限查询的开关，如果不设置，默认为false，查询不到权限
        jdbcRealm.setPermissionsLookupEnabled(true);


        //设置自己的认证的查询预计，不使用JdbcRealm默认的sql语句
//        jdbcRealm.setAuthenticationQuery("");

        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(jdbcRealm);

        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("LanCoder","123456");
        subject.login(token);
        System.out.println("isAuthored:"+subject.isAuthenticated());
        subject.checkRole("admin");
        subject.checkPermission("user:select");
    }
}
