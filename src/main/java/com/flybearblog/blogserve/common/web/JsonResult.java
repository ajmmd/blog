package com.flybearblog.blogserve.common.web;

import com.flybearblog.blogserve.common.enumerator.ServiceCode;
import com.flybearblog.blogserve.common.ex.ServiceException;
import lombok.Data;

import java.io.Serializable;

/**
 * 服务器端的统一响应类型
 *
 * @author java@tedu.cn
 * @version 3.0
 */
@Data
public class JsonResult implements Serializable {

    /**
     * 操作结果的状态码（状态标识）
     */
    private Integer state;

    /**
     * 操作失败时的提示文本
     */
    private String message;

    /**
     * 操作成功时响应的数据
     */
    private Object data;

    /**
     * 生成表示"成功"的响应对象
     *
     * @return 表示"成功"的响应对象
     */
    public static JsonResult ok() {
        return ok(null);
    }

    /**
     * 生成表示"成功"的响应对象，此对象中将包含响应到客户端的数据
     *
     * @param data 响应到客户端的数据
     * @return 表示"成功"的响应对象
     */
    public static JsonResult ok(Object data) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.state = ServiceCode.OK.getValue();
        jsonResult.data = data;
        return jsonResult;
    }

    /**
     * 生成表示"失败"的响应对象
     *
     * @param e 业务异常
     * @return 表示"失败"的响应对象
     */
    public static JsonResult fail(ServiceException e) {
        return fail(e.getServiceCode(), e.getMessage());
    }

    /**
     * 生成表示"失败"的响应对象
     *
     * @param serviceCode 业务状态码
     * @param message     提示文本
     * @return 表示"失败"的响应对象
     */
    public static JsonResult fail(ServiceCode serviceCode, String message) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.state = serviceCode.getValue();
        jsonResult.message = message;
        return jsonResult;
    }

}
