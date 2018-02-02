package com.ttb.ShiroOne;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;

/**
 * @version: 1.0.0
 * @author: wangcd
 * @date: 二月 02 2018,14:03
 * @description:
 **/
public class MyRealm1 implements Realm {
    @Override
    public String getName() {
        return "MyRealm1";
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        String password = new String((char[])token.getCredentials());
        if(!"zhang".equalsIgnoreCase(username)){
            throw new UnknownAccountException("用户名错误");
        }
        if(!"123".equalsIgnoreCase(password)){
            throw new IncorrectCredentialsException("密码错误");
        }
        return new SimpleAuthenticationInfo(username,password,getName());
    }
}
