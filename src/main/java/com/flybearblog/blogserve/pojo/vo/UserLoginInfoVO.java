package com.flybearblog.blogserve.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserLoginInfoVO implements Serializable {

    /**
     * 数据id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码（密文）
     */
    private String password;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 是否启用，1=启用，0=未启用
     */
    private Integer enable;

    /**
     * 权限列表
     */
    private List<String> authorities;

}
