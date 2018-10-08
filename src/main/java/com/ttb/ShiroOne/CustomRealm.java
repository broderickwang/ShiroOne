package com.ttb.ShiroOne;

import com.ttb.dao.UserDao;
import com.ttb.domain.UserDO;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @project: ShiroOne
 * @author: wangchengda
 * @email: wangchengda1990@gmail.com
 * @date: 2018/9/18
 * @Description:
 * @Copyright: 2018 broderickwang.github.io Inc. All rights reserved.
 * @version: V1.0
 */
public class CustomRealm extends AuthorizingRealm {
    Map<String,String> users = new HashMap<>();
    {
        users.put("LanCoder","f80471e095c8588af10099dec3ed816d");
    }

    @Autowired
    UserDao userDao;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //授权
        String username = (String) principalCollection.getPrimaryPrincipal();
        Set<String> roles = getRolesByUsername(username);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo(roles);
        Set<String> permissions = gerPermissionsByUsername(username);
        simpleAuthorizationInfo.setStringPermissions(permissions);

        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //认证
        //1. 从主体传过来的认证消息中，获取用户名
        String username = (String) authenticationToken.getPrincipal();
        //2. 通过用户名去数据库中获取凭证
        String password = getPasswordByUsername(username);
        if (password==null){
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username,password,"costomRealm");

        //返回的认证 加盐
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("LanCoder"));

        return authenticationInfo;
    }

    private String getPasswordByUsername(String username) {
        // TODO: 2018/9/18 模拟数据库，获取密码
//        return users.get(username);
        UserDO userDO = userDao.getOneByName(username);
        return userDO.getPassword();
    }

    private Set<String> gerPermissionsByUsername(String username) {
        // TODO: 2018/9/18 从数据库中获取用户的权限信息
        Set<String> permissions = new HashSet<>();
        permissions.add("sys:add:user");
        return permissions;
    }

    private Set<String> getRolesByUsername(String username) {
        // TODO: 2018/9/18 从数据库中获取用户的角色信息
        Set<String> roles = new HashSet<>();
        /*roles.add("admin");
        roles.add("teacher");*/
        List<String> rs = userDao.getRoleByUsername(username);
        roles.addAll(rs);
        return roles;
    }

    public static void main(String[] args){
        //密码 盐
        Md5Hash md5Hash = new Md5Hash("111111","LanCoder");
        System.out.print(md5Hash.toString());
    }
}
