package com.ttb.session;

import com.ttb.utils.JedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
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
public class RedisSessionDao extends AbstractSessionDAO {

    private static final String SESSION_PREFIX = "LanCoder:";

    JedisUtil jedisUtil;

    public void setJedisUtil(JedisUtil jedisUtil) {
        this.jedisUtil = jedisUtil;
        /**
         * 初始化redisManager
         */
        this.jedisUtil.init();
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        //将session与sessionId进行捆绑
        assignSessionId(session,sessionId);
        saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable serializable) {
        System.out.print("read session");
        if(serializable == null){
            return null;
        }

        Session s = (Session)SerializationUtils.deserialize(jedisUtil.get(this.getBytes(serializable)));
        return s;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);
    }

    @Override
    public void delete(Session session) {
        if (session==null || session.getId()==null)
            return;
        jedisUtil.del(getBytes(session.getId()));
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<Session> sessions = new HashSet<Session>();

        Set<byte[]> keys = jedisUtil.keys(SESSION_PREFIX + "*");
        if(keys != null && keys.size()>0){
            for(byte[] key:keys){
                Session s = (Session)SerializationUtils.deserialize(jedisUtil.get(key));
                sessions.add(s);
            }
        }

        return sessions;
    }

    private void saveSession(Session session){
        if (session==null || session.getId()==null){
            return;
        }
        byte[] key = getBytes(session.getId());
        byte[] value = SerializationUtils.serialize(session);
        jedisUtil.set(key,value,100);
    }

    private byte[] getBytes(Serializable str){
        String tmp = SESSION_PREFIX+str;
        return tmp.getBytes();
    }
}
