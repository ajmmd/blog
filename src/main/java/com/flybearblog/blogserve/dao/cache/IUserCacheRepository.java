package com.flybearblog.blogserve.dao.cache;


import com.flybearblog.blogserve.common.cache.PassportConsts;
import com.flybearblog.blogserve.common.pojo.po.UserStatePO;

public interface IUserCacheRepository extends PassportConsts {

    void saveUserState(Long userId, UserStatePO userStatePO);

    Boolean deleteUserState(Long userId);

    void renewal(Long userId);

    void setUserDisabled(Long userId);

    UserStatePO getUserState(Long userId);

}
