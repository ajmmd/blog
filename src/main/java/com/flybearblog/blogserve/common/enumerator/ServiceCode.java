package com.flybearblog.blogserve.common.enumerator;

/**
 * 业务状态码
 *
 * @author java@tedu.cn
 * @version 3.0
 */
public enum ServiceCode {

    /**
     * 操作成功
     */
    OK(20000),
    /**
     * 错误：请求参数格式错误
     */
    ERROR_BAD_REQUEST(40000),
    /**
     * 错误：未认证
     */
    ERROR_UNAUTHORIZED(40100),
    /**
     * 错误：未认证，因为被禁用
     */
    ERROR_UNAUTHORIZED_DISABLED(40101),
    /**
     * 错误：禁止访问，用于无权限
     */
    ERROR_FORBIDDEN(40300),
    /**
     * 错误：数据不存在
     */
    ERROR_NOT_FOUND(40400),
    /**
     * 错误：数据冲突
     */
    ERROR_CONFLICT(40900),
    /**
     * 错误：未知的插入数据失败
     */
    ERROR_INSERT(50000),
    /**
     * 错误：未知的删除数据失败
     */
    ERROR_DELETE(50100),
    /**
     * 错误：未知的修改数据失败
     */
    ERROR_UPDATE(50200),
    /**
     * 错误：JWT已过期
     */
    ERR_JWT_EXPIRED(60000),
    /**
     * 错误：JWT验证签名失败，可能使用了伪造的JWT
     */
    ERR_JWT_SIGNATURE(60100),
    /**
     * 错误：JWT格式错误
     */
    ERR_JWT_MALFORMED(60200),
    /**
     * 错误：上传的文件为空（没有选择有效的文件）
     */
    ERROR_UPLOAD_EMPTY(90000),
    /**
     * 错误：上传的文件类型有误
     */
    ERROR_UPLOAD_INVALID_TYPE(90100),
    /**
     * 错误：上传的文件超出限制
     */
    ERROR_UPLOAD_EXCEED_MAX_SIZE(90200),
    /**
     * 错误：其它异常
     */
    ERROR_UNKNOWN(99999);

    /**
     * 枚举对象的值
     */
    private Integer value;

    ServiceCode(Integer value) {
        this.value = value;
    }

    /**
     * 获取枚举对象的值
     *
     * @return 枚举对象的值
     */
    public Integer getValue() {
        return value;
    }

}