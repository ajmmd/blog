package com.flybearblog.blogserve.service.impl;


import com.alibaba.fastjson.JSON;
import com.flybearblog.blogserve.common.enumerator.ServiceCode;
import com.flybearblog.blogserve.common.ex.ServiceException;
import com.flybearblog.blogserve.common.pojo.authentication.CurrentPrincipal;
import com.flybearblog.blogserve.common.pojo.po.UserStatePO;
import com.flybearblog.blogserve.dao.cache.IUserCacheRepository;
import com.flybearblog.blogserve.dao.persist.repository.IUserRepository;
import com.flybearblog.blogserve.pojo.param.UserLoginInfoParam;
import com.flybearblog.blogserve.pojo.vo.UserLoginInfoVO;
import com.flybearblog.blogserve.pojo.vo.UserLoginResultVO;
import com.flybearblog.blogserve.service.IUserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Value("${tmall.jwt.secret-key}")
    private String secretKey;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IUserCacheRepository userCacheRepository;

    @Override
    public UserLoginResultVO login(UserLoginInfoParam userLoginInfoParam, String remoteAddr, String userAgent) {
        // 从参数中获取用户名
        String username = userLoginInfoParam.getUsername();
        // 调用Repository根据用户名查询用户信息
        UserLoginInfoVO queryResult = userRepository.getLoginInfoByUsername(username);
        // 判断查询结果是否为null
        if (queryResult == null) {
            // 是：抛出异常（UNAUTHORIZED，账号不存在）
            String message = "登录失败，用户名或密码错误！【1】";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_UNAUTHORIZED, message);
        }

        // 判断查询结果中的enable是否不为1
        if (queryResult.getEnable() != 1) {
            // 是：抛出异常（UNAUTHORIZED_DISABLED，账号已经被禁用）
            String message = "登录失败，此账号已经被禁用！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_UNAUTHORIZED_DISABLED, message);
        }

        // 从参数中获取密码
        String password = userLoginInfoParam.getPassword();
        // 判断查询结果中的密码，与以上参数密码，是否不匹配
        if (!passwordEncoder.matches(password, queryResult.getPassword())) {
            // 是：抛出异常（UNAUTHORIZED，密码错误）
            String message = "登录失败，用户名或密码错误！【2】";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_UNAUTHORIZED, message);
        }

        // 如果代码能够执行到此处，则表示登录成功
        // TODO 向登录日志表中插入数据
        // TODO 更新当前用户的最后登录信息（最后登录时间、最后登录IP地址、累计登录次数）
        // TODO 其它

        // 准备返回的结果数据
        UserLoginResultVO loginResult = new UserLoginResultVO();
        BeanUtils.copyProperties(queryResult, loginResult);

        // 生成当前用户对应的JWT，并保存到UserLoginResultVO中
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", loginResult.getId());
        claims.put("username", loginResult.getUsername());
        claims.put("remoteAddr", remoteAddr);
        claims.put("userAgent", userAgent);
        String jwt = Jwts.builder()
                // Header
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                // Payload
                .setClaims(claims)
                // .setExpiration(new Date(System.currentTimeMillis() + 60 * 1000))
                // Verify Signature
                .signWith(SignatureAlgorithm.HS256, secretKey)
                // Done
                .compact();
        loginResult.setToken(jwt);

        // 将用户的相关数据写入到Redis中
        List<String> authorities = loginResult.getAuthorities();
        String authoritiesJsonString = JSON.toJSONString(authorities);
        UserStatePO userStatePO = new UserStatePO();
        userStatePO.setAuthoritiesJsonString(authoritiesJsonString);
        userStatePO.setEnable(1);
        userCacheRepository.saveUserState(loginResult.getId(), userStatePO);

        // 返回
        return loginResult;
    }

    @Override
    public void logout(CurrentPrincipal principal) {
        Long userId = principal.getId();
        userCacheRepository.deleteUserState(userId);
    }

}
