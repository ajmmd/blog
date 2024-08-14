package com.flybearblog.blogserve.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserLoginResultVO implements Serializable {

    /**
     * 数据id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * Token
     */
    private String token;

    /**
     * 权限列表
     */
    private List<String> authorities;

}
