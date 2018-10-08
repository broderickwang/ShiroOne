package com.ttb.utils;

import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


import java.util.Set;

/**
 * @project: ShiroOne
 * @author: wangchengda
 * @email: wangchengda1990@gmail.com
 * @date: 2018/10/8
 * @Description:
 * @Copyright: 2018 broderickwang.github.io Inc. All rights reserved.
 * @version: V1.0
 */
public class JedisUtil {
    @Value("${spring.redis.host}")
    private String host = "127.0.0.1";

    @Value("${spring.redis.port}")
    private int port = 6379;

    // 0 - never expire
    private int expire = 0;

    //timeout for jedis try to connect to redis server, not expire time! In milliseconds
    @Value("${spring.redis.timeout}")
    private int timeout = 0;

    @Value("${spring.redis.password}")
    private String password = "";

    private static JedisPool jedisPool = null;

    public JedisUtil() {

    }

    /**
     * 初始化方法
     */
    public void init() {
        if (jedisPool == null) {
            if (password != null && !"".equals(password)) {
                jedisPool = new JedisPool(new JedisPoolConfig(), host, port, timeout, password);
            } else if (timeout != 0) {
                jedisPool = new JedisPool(new JedisPoolConfig(), host, port, timeout);
            } else {
                jedisPool = new JedisPool(new JedisPoolConfig(), host, port);
            }

        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 根据key从redis中获取session
     * @param key
     * @return
     */
    public byte[] get(byte[] key){
        byte[] value = null;
        Jedis jedis = jedisPool.getResource();
        try {
            value = jedis.get(key);
        } finally {
            if (jedis != null) {
//                jedis.close();
            }
        }
        return value;
    }

    /**
     * 设置session
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public byte[] set(byte[] key, byte[] value, int expire) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
            if (expire != 0) {
                jedis.expire(key, expire);
            }
        } finally {
            if (jedis != null) {
//                jedis.close();
            }
        }
        return value;
    }

    /**
     * 根据key从redis中删除session
     * @param bytes
     */
    public void del(byte[] bytes) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del(bytes);
        } finally {
            if (jedis != null) {
//                jedis.close();
            }
        }

    }

    public Set<byte[]> keys(String s) {
        Set<byte[]> sets = null;
        Jedis jedis = jedisPool.getResource();
        try {
            sets = jedis.keys(s.getBytes());
        }finally {
            if (jedis != null) {
//                jedis.close();
            }
        }
        return sets;
    }
}
