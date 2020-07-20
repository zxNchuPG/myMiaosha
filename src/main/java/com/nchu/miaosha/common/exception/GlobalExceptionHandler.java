package com.nchu.miaosha.common.exception;

import com.nchu.miaosha.common.response.CodeMsg;
import com.nchu.miaosha.common.response.ApiResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName: GlobalExceptionHandler
 * @Author: 时间
 * @Description: 全局异常处理器, 可以让前端显示更友好
 * @Date: 2020/7/14 23:15
 * @Version: 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ApiResponse<String> exceptionHandler(HttpServletRequest request, Exception e) {
        e.printStackTrace();
        if (e instanceof GlobalException) {
            GlobalException ex = (GlobalException) e;
            return ApiResponse.error(ex.getCodeMsg());
        } else if (e instanceof BindException) {
            BindException ex = (BindException) e;
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            return ApiResponse.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        } else {
            return ApiResponse.error(CodeMsg.SERVER_ERROR);
        }
    }

}
