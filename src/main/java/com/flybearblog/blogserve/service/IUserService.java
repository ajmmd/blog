package com.flybearblog.blogserve.service;

import com.flybearblog.blogserve.common.pojo.authentication.CurrentPrincipal;
import com.flybearblog.blogserve.pojo.param.UserLoginInfoParam;
import com.flybearblog.blogserve.pojo.vo.UserLoginResultVO;

public interface IUserService {

    UserLoginResultVO login(UserLoginInfoParam userLoginInfoParam, String remoteAddr, String userAgent);

    void logout(CurrentPrincipal principal);

}
