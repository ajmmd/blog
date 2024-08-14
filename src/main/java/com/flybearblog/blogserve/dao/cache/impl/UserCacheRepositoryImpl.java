package com.flybearblog.blogserve.dao.cache.impl;

import cn.hutool.core.bean.BeanUtil;
import com.flybearblog.blogserve.common.pojo.po.UserStatePO;
import com.flybearblog.blogserve.dao.cache.IUserCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Repository
public class UserCacheRepositoryImpl implements IUserCacheRepository {

    @Value("${tmall.authenticate.duration-in-minute}")
    private Long durationInMinute;
    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Override
    public void saveUserState(Long userId, UserStatePO userStatePO) {
        String key = KEY_PREFIX_USER_STATE + userId;
        HashOperations<String, Object, Object> opsForHash = redisTemplate.opsForHash();
        Map<String, Object> map = BeanUtil.beanToMap(userStatePO);
        opsForHash.putAll(key, map);
        renewal(userId);
    }

    @Override
    public Boolean deleteUserState(Long userId) {
        String key = KEY_PREFIX_USER_STATE + userId;
        return redisTemplate.delete(key);
    }

    @Override
    public void renewal(Long userId) {
        String key = KEY_PREFIX_USER_STATE + userId;
        redisTemplate.expire(key, durationInMinute, TimeUnit.MINUTES);
    }

    @Override
    public void setUserDisabled(Long userId) {
        String key = KEY_PREFIX_USER_STATE + userId;
        HashOperations<String, Object, Object> opsForHash = redisTemplate.opsForHash();
        opsForHash.put(key, "enable", 0);
    }

    @Override
    public UserStatePO getUserState(Long userId) {
        String key = KEY_PREFIX_USER_STATE + userId;
        HashOperations<String, Object, Object> opsForHash = redisTemplate.opsForHash();
        Map<Object, Object> map = opsForHash.entries(key);
        if (!map.isEmpty()) {
            return BeanUtil.mapToBean(map, UserStatePO.class, true, null);
        }
        return null;
    }

}
