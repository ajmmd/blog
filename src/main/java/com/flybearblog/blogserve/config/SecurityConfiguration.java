package com.flybearblog.blogserve.config;

import com.alibaba.fastjson.JSON;
import com.flybearblog.blogserve.common.enumerator.ServiceCode;
import com.flybearblog.blogserve.common.web.JsonResult;
import com.flybearblog.blogserve.filter.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.PrintWriter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 允许跨域访问
        http.cors();

        // 调整Session的创建策略，关于配置值：
        // -- NEVER：从不主动创建，但是，如果Session存在，仍会使用
        // -- STATELESS：无状态，则始终不使用Session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 将解析JWT的过滤器，配置在Spring Security的相关过滤器之前
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        // 处理“未通过认证时，却访问需要认证的资源”的异常
        // http.exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
        //    @Override
        //    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //
        //    }
        // });
        http.exceptionHandling().authenticationEntryPoint((request, response, e) -> {
            response.setContentType("application/json; charset=UTF-8");
            String message = "您当前没有登录，或登录已过期！";
            JsonResult jsonResult = JsonResult.fail(ServiceCode.ERROR_UNAUTHORIZED, message);
            String jsonString = JSON.toJSONString(jsonResult);
            PrintWriter writer = response.getWriter();
            writer.println(jsonString);
            writer.close();
            return;
        });

        // 禁用“防止伪造的跨域攻击的防御机制”
        http.csrf().disable();

        // 白名单
        String[] urls = {
                "/doc.html",
                "/**/*.css",
                "/**/*.js",
                "/swagger-resources",
                "/v2/api-docs",
                "/favicon.ico",
                "/passport/login"
        };

        // 配置请求授权
        http.authorizeRequests() // 开始配置授权访问
                .mvcMatchers(urls) // 匹配某些请求
                .permitAll() // 许可，不需要通过认证就可以访问
                .anyRequest() // 任何请求，实际表现为：除了以上配置过的以外的那些请求
                .authenticated() // 需要以上请求是已经通过认证的
        ;

        // 登录页
        // 调用formLogin()方法，表示启用登录页，当“未认证”时，访问那些“需要通过认证”的请求将被重定向到登录页
        // 不调用formLogin()方法，表示不启用登录页，当“未认证”时，访问那些“需要通过认证”的请求将被响应403错误
        // http.formLogin();
    }

}
