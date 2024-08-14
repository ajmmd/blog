package com.flybearblog.blogserve.handler;

import com.flybearblog.blogserve.common.enumerator.ServiceCode;
import com.flybearblog.blogserve.common.ex.ServiceException;
import com.flybearblog.blogserve.common.web.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public JsonResult handleServiceException(ServiceException e) {
        return JsonResult.fail(e);
    }

    @ExceptionHandler
    public JsonResult handleBindException(BindException e) {
//        String message = "";
//        List<FieldError> fieldErrors = e.getFieldErrors();
//        for (FieldError fieldError : fieldErrors) {
//            String defaultMessage = fieldError.getDefaultMessage();
//            message += defaultMessage;
//        }
        String message = e.getFieldError().getDefaultMessage();
        return JsonResult.fail(ServiceCode.ERROR_BAD_REQUEST, message);
    }

    @ExceptionHandler
    public JsonResult handleConstraintViolationException(ConstraintViolationException e) {
        String message = "";
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            message = constraintViolation.getMessage();
        }
        return JsonResult.fail(ServiceCode.ERROR_BAD_REQUEST, message);
    }

    @ExceptionHandler
    public JsonResult handleAccessDeniedException(AccessDeniedException e) {
        String message = "您当前登录的账号无此操作权限！";
        return JsonResult.fail(ServiceCode.ERROR_FORBIDDEN, message);
    }

    @ExceptionHandler
    public JsonResult handleThrowable(Throwable e) {
        String message = "服务器忙，请稍后再试！【同学们，在开发时，如果看到这句话，你应该在服务器端控制台查看实际出现的异常及相关信息，并在全局异常处理器中补充对此异常的处理】";
        // e.printStackTrace(); // 在生产环境中禁止使用此语句输出异常信息
        log.warn("", e);
        return JsonResult.fail(ServiceCode.ERROR_UNKNOWN, message);
    }

}
