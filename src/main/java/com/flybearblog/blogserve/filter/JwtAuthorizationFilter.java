package com.flybearblog.blogserve.filter;

import com.alibaba.fastjson.JSON;
import com.flybearblog.blogserve.common.enumerator.ServiceCode;
import com.flybearblog.blogserve.common.pojo.authentication.CurrentPrincipal;
import com.flybearblog.blogserve.common.pojo.po.UserStatePO;
import com.flybearblog.blogserve.common.web.JsonResult;
import com.flybearblog.blogserve.dao.cache.IUserCacheRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// 处理JWT的过滤器，其主要职责包括：
// 1. 尝试接收客户端携带的JWT
// 2. 尝试解析接收到的JWT
// 3. 将成功解析后的结果用于创建Authentication对象，并存入到SecurityContext中
@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Value("${blog.jwt.secret-key}")
    private String secretKey;
    @Autowired
    private IUserCacheRepository userCacheRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/users/login")
                || requestURI.endsWith(".css")
                || requestURI.endsWith(".js")
                || requestURI.equals("/doc.html")) {
            // 放行
            filterChain.doFilter(request, response);
            // 结束
            return;
        }
        System.out.println("处理JWT的过滤器开始处理请求：" + requestURI);

        // 根据业内的惯例，客户端应该将JWT放在请求头（Request Header）中名为Authorization的属性中
        String jwt = request.getHeader("Authorization");
        System.out.println("客户端携带的JWT：" + jwt);

        // 如果客户端没有携带有效的JWT，则放行，此请求会继续经由后续的过滤器（例如Spring Security框架的过滤器）进行处理
        if (jwt == null) {
            // 放行
            filterChain.doFilter(request, response);
            // 结束
            return;
        }

        // 尝试解析JWT
        response.setContentType("application/json; charset=UTF-8");
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();
        } catch (MalformedJwtException e) {
            String message = "非法访问！【1】";
            System.out.println(message);
            JsonResult jsonResult = JsonResult.fail(ServiceCode.ERR_JWT_MALFORMED, message);
            String jsonString = JSON.toJSONString(jsonResult);
            PrintWriter writer = response.getWriter();
            writer.println(jsonString);
            writer.close();
            return;
        } catch (SignatureException e) {
            String message = "非法访问！【2】";
            System.out.println(message);
            JsonResult jsonResult = JsonResult.fail(ServiceCode.ERR_JWT_SIGNATURE, message);
            String jsonString = JSON.toJSONString(jsonResult);
            PrintWriter writer = response.getWriter();
            writer.println(jsonString);
            writer.close();
            return;
        } catch (Throwable e) {
            String message = "服务器忙，请稍后再试！【同学们，在开发时，如果看到这句话，你应该在服务器端控制台查看实际出现的异常及相关信息，并在处理JWT的过滤器中补充对此异常的处理】";
            log.warn("", e);
            JsonResult jsonResult = JsonResult.fail(ServiceCode.ERROR_UNKNOWN, message);
            String jsonString = JSON.toJSONString(jsonResult);
            PrintWriter writer = response.getWriter();
            writer.println(jsonString);
            writer.close();
            return;
        }

        // 获取JWT中的数据
        Long userId = claims.get("id", Long.class);
        String username = claims.get("username", String.class);
        String remoteAddr = claims.get("remoteAddr", String.class);
        String userAgent = claims.get("userAgent", String.class);
        System.out.println("解析JWT的结果：userId = " + userId);
        System.out.println("解析JWT的结果：username = " + username);
        System.out.println("解析JWT的结果：remoteAddr = " + remoteAddr);
        System.out.println("解析JWT的结果：userAgent = " + userAgent);

        // 检查是否盗用
        String currentRemoteAddr = request.getRemoteAddr();
        String currentUserAgent = request.getHeader("User-Agent");
        if (!currentRemoteAddr.equals(remoteAddr) && !currentUserAgent.equals(userAgent)) {
            System.out.println("本次来访的IP地址与浏览器信息，与此前登录时的均不匹配，本次视为未认证");
            filterChain.doFilter(request, response);
            return;
        }

        // 获取此用户在Redis中的数据
        UserStatePO userState = userCacheRepository.getUserState(userId);
        if (userState == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 检查用户的账号状态
        if (userState.getEnable() != 1) {
            System.out.println("用户账号已经被禁用，不允许继续访问！");
            String message = "您的账号已经被禁用，将强制下线！";
            JsonResult jsonResult = JsonResult.fail(ServiceCode.ERROR_UNAUTHORIZED_DISABLED, message);
            String jsonString = JSON.toJSONString(jsonResult);
            PrintWriter writer = response.getWriter();
            writer.println(jsonString);
            writer.close();
            userCacheRepository.deleteUserState(userId);
            return;
        }

        // 对Redis中的用户数据进行续期
        userCacheRepository.renewal(userId);

        // 创建Security框架将使用到的当事人
        Object principal = new CurrentPrincipal(userId, username);

        // 创建Security框架将使用到的权限列表
        String authoritiesJsonString = userState.getAuthoritiesJsonString();
        List<String> stringList = JSON.parseArray(authoritiesJsonString, String.class);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (String s : stringList) {
            authorities.add(new SimpleGrantedAuthority(s));
        }

        // 向SecurityContext中存入Authentication数据
        Authentication authentication
                = new UsernamePasswordAuthenticationToken(principal, null, authorities);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        // 放行
        filterChain.doFilter(request, response);
    }

}
