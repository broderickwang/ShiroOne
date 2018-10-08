package com.ttb.config;

import com.ttb.ShiroOne.CustomRealm;
import com.ttb.session.CustomWebSessionManager;
import com.ttb.session.RedisSessionDao;
import com.ttb.filter.RolesOrFilter;
import com.ttb.utils.JedisUtil;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @project: ShiroOne
 * @author: wangchengda
 * @email: wangchengda1990@gmail.com
 * @date: 2018/10/8
 * @Description:
 * @Copyright: 2018 broderickwang.github.io Inc. All rights reserved.
 * @version: V1.0
 */
@Configuration
public class ShiroConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.timeout}")
    private int timeout;

    @Bean
    Filter getFilter(){
        return new RolesOrFilter();
    }

    @Bean
    ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        filterChainDefinitionMap.put("/login","anon");
        filterChainDefinitionMap.put("/subLogin","anon");
        filterChainDefinitionMap.put("/403","anon");
        filterChainDefinitionMap.put("/test/role1","roleOrFilter['admin1','admin']");
        filterChainDefinitionMap.put("/**", "authc");
        // TODO: 2018/10/8 修改自定义的filter
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("roleOrFilter",getFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置realm.
        securityManager.setRealm(userRealm());
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    @Bean
    CustomRealm userRealm(){
        CustomRealm customRealm = new CustomRealm();
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");    // 设置加密的算法
        matcher.setHashIterations(1);       //设置加密的次数
        //设置加密
        customRealm.setCredentialsMatcher(matcher);
        return customRealm;
    }

    //--------------通过注解配置授权
    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        return new DefaultAdvisorAutoProxyCreator();
    }

    //--- redis 缓存 ， shiro会话管理
    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public RedisSessionDao redisSessionDAO() {
        RedisSessionDao redisSessionDAO = new RedisSessionDao();
        redisSessionDAO.setJedisUtil(jedisUtil());
        return redisSessionDAO;
    }

    @Bean
    public JedisUtil jedisUtil() {
        JedisUtil jedisUtil = new JedisUtil();
        jedisUtil.setHost(host);
        jedisUtil.setPort(port);
        jedisUtil.setExpire(1800);// 配置缓存过期时间
        //redisManager.setTimeout(1800);
        jedisUtil.setPassword(password);
        return jedisUtil;
    }

    @Bean
    public DefaultWebSessionManager sessionManager(){
        CustomWebSessionManager sessionManager = new CustomWebSessionManager();
//        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }

}
