package com.flybearblog.blogserve.dao.persist.repository;


import com.flybearblog.blogserve.pojo.vo.UserLoginInfoVO;

public interface IUserRepository {

    UserLoginInfoVO getLoginInfoByUsername(String username);

}
