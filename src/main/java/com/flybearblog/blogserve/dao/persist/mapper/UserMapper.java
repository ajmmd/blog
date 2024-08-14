package com.flybearblog.blogserve.dao.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flybearblog.blogserve.pojo.entity.User;
import com.flybearblog.blogserve.pojo.vo.UserLoginInfoVO;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {

    UserLoginInfoVO getLoginInfoByUsername(String username);

}
