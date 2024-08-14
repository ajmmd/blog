package com.flybearblog.blogserve.controller;

import com.flybearblog.blogserve.common.pojo.authentication.CurrentPrincipal;
import com.flybearblog.blogserve.pojo.vo.UserLoginResultVO;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/test")
@Api(tags = "2. 测试访问")
public class TestController {

    @Deprecated
    @GetMapping("/session/authenticate")
    @ApiOperation("【已过期】基于Session执行认证")
    @ApiOperationSupport(order = 100)
    public String authenticate(@ApiIgnore HttpSession session) {
        // 从Session中取出数据
        Object object = session.getAttribute("loginResult");
        if (object == null) {
            return "您还没有登录，请先登录！";
        }
        UserLoginResultVO loginResult = (UserLoginResultVO) object;
        return "您当前登录的用户名是：" + loginResult.getUsername();
    }

    @Deprecated
    @GetMapping("/session/authorize")
    @ApiOperation("【已过期】基于Session授权访问")
    @ApiOperationSupport(order = 200)
    public String authorize(@ApiIgnore HttpSession session) {
        // 从Session中取出数据
        Object object = session.getAttribute("loginResult");
        if (object == null) {
            return "您还没有登录，请先登录！";
        }
        UserLoginResultVO loginResult = (UserLoginResultVO) object;
        List<String> authorities = loginResult.getAuthorities();
        // 检查权限，假设需要 /account/user/delete 权限
        if (!authorities.contains("/account/user/delete")) {
            return "您已登录，但无此操作权限！";
        }
        return "您当前登录的账号可以成功访问！用户信息：" + loginResult.getUsername();
    }

    @GetMapping("/security/authenticate")
    @ApiOperation("基于Security执行认证")
    @ApiOperationSupport(order = 300)
    public String securityAuthenticate() {
        return "您已经通过了Spring Security的认证！";
    }

    @GetMapping("/security/principal")
    @ApiOperation("基于Security识别当事人")
    @ApiOperationSupport(order = 400)
    public String securityPrincipal(@ApiIgnore @AuthenticationPrincipal CurrentPrincipal principal) {
        return "您已经通过了Spring Security的认证！当事人ID：" + principal.getId()
                + "，当事人用户名：" + principal.getUsername();
    }

    @GetMapping("/security/authorize")
    @PreAuthorize("hasAuthority('/account/user/delete')") // 假设需要 /account/user/delete 权限
    @ApiOperation("基于Security处理授权")
    @ApiOperationSupport(order = 500)
    public String securityAuthorize() {
        return "您已经通过了Spring Security的认证与授权！";
    }

}
