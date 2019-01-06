package com.hucheng.cfms.entity;

public class ResultVO<T> {
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";
    public static final String NO_MSG = "NO_MSG";
    public static final String NO_DATA = "NO_DATA";
    // 响应结果：SUCCESS和FAILED
    private String result;

    // 在响应结果为FAILED时的提示消息，默认情况下没有消息
    private String message = NO_MSG;

    // 在响应结果为SUCESS时返回的数据
    private T data;

    public ResultVO() {

    }

    public ResultVO(String result, String message, T data) {

        this.result = result;
        this.message = message;
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultVO [result=" + result + ", message=" + message + ", data=" + data + "]";
    }

}
