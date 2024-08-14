package com.flybearblog.blogserve.dao.persist.repository.impl;

import com.flybearblog.blogserve.dao.persist.mapper.UserMapper;
import com.flybearblog.blogserve.dao.persist.repository.IUserRepository;
import com.flybearblog.blogserve.pojo.vo.UserLoginInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements IUserRepository {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserLoginInfoVO getLoginInfoByUsername(String username) {
        return userMapper.getLoginInfoByUsername(username);
    }

}
