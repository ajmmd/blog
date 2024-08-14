package com.flybearblog.blogserve.common.ex;

import com.flybearblog.blogserve.common.enumerator.ServiceCode;
import lombok.Getter;

/**
 * 业务异常
 *
 * @author java@tedu.cn
 * @version 3.0
 */
public class ServiceException extends RuntimeException {

    @Getter
    private ServiceCode serviceCode;

    /**
     * 创建业务异常对象
     *
     * @param serviceCode 业务状态码
     * @param message     描述文本
     */
    public ServiceException(ServiceCode serviceCode, String message) {
        super(message);
        this.serviceCode = serviceCode;
    }

}
