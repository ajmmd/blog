package com.flybearblog.blogserve.controller;

import com.flybearblog.blogserve.common.pojo.authentication.CurrentPrincipal;
import com.flybearblog.blogserve.common.web.JsonResult;
import com.flybearblog.blogserve.pojo.param.UserLoginInfoParam;
import com.flybearblog.blogserve.pojo.vo.UserLoginResultVO;
import com.flybearblog.blogserve.service.IUserService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/passport")
@Api(tags = "1. 单点登录")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    @ApiOperation("用户登录")
    @ApiOperationSupport(order = 100)
    public JsonResult login(@Valid UserLoginInfoParam userLoginInfoParam,
                            @ApiIgnore HttpServletRequest request) {
        // 获取用户的IP地址和浏览器信息
        String remoteAddr = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        System.out.println("IP地址：" + remoteAddr);
        System.out.println("浏览器信息：" + userAgent);
        // 调用Service执行验证登录
        UserLoginResultVO loginResult = userService.login(userLoginInfoParam, remoteAddr, userAgent);
        // 登录成功，向Session中存入数据
        // session.setAttribute("loginResult", loginResult);

//        // 创建Security框架将使用到的当事人
//        Object principal = new CurrentPrincipal(loginResult.getId(), loginResult.getUsername());
//
//        // 创建Security框架将使用到的权限列表
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        for (String authority : loginResult.getAuthorities()) {
//            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority);
//            authorities.add(grantedAuthority);
//        }
//
//        // 向SecurityContext中存入Authentication数据
//        Authentication authentication
//                = new UsernamePasswordAuthenticationToken(principal, null, authorities);
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        securityContext.setAuthentication(authentication);

        return JsonResult.ok(loginResult);
    }

    @PostMapping("/logout")
    @ApiOperation("退出登录")
    @ApiOperationSupport(order = 200)
    public JsonResult logout(@AuthenticationPrincipal @ApiIgnore CurrentPrincipal principal) {
        userService.logout(principal);
        return JsonResult.ok();
    }

}