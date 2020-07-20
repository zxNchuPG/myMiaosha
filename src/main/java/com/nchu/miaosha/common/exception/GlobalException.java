package com.nchu.miaosha.common.exception;

import com.nchu.miaosha.common.response.CodeMsg;

/**
 * @ClassName: GlobalException
 * @Author: 时间
 * @Description: 全局异常
 * @Date: 2020/7/14 23:04
 * @Version: 1.0
 */
public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private CodeMsg codeMsg;

    public GlobalException(CodeMsg codeMsg) {
        super(codeMsg.toString());
        this.codeMsg = codeMsg;
    }

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }

    public void setCodeMsg(CodeMsg codeMsg) {
        this.codeMsg = codeMsg;
    }
}
