package com.nchu.miaosha.common.response;

public class ApiResponse<T> {
    private int code;
    private String msg;
    private T data;

    /**
     * 成功时候的调用
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<T>(data);
    }

    /**
     * 失败时候的调用
     */
    public static <T> ApiResponse<T> error(CodeMsg cm) {
        return new ApiResponse<T>(cm);
    }

    private ApiResponse(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }

    private ApiResponse(CodeMsg cm) {
        if (cm == null) {
            return;
        }
        this.code = cm.getCode();
        this.msg = cm.getMsg();
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}
