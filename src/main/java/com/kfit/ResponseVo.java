package com.kfit;

import java.io.Serializable;

public class ResponseVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code;
    private String msg;
    private Object data;

    private final static String CODE_OK = "0";
    private final static String CODE_ERROR = "1";
    private final static String MSG_OK = "操作成功！";
    private final static String MSG_ERROR = "数据请求失败，请稍后再试！";

    private final static String CODE_TOKEN_ERROR = "000001";

    public ResponseVo(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseVo() {
    }

    /**
     * 通用失败返回操作
     *
     * @param result 返回数据 可为null
     * @return
     */
    public static ResponseVo getErrorResult(Object result) {
        return new ResponseVo(ResponseVo.CODE_ERROR, ResponseVo.MSG_ERROR, result);
    }

    public static ResponseVo getErrorResult(String MSG) {
        return new ResponseVo(ResponseVo.CODE_ERROR, MSG, null);
    }

    public static ResponseVo getErrorResult() {
        return new ResponseVo(ResponseVo.CODE_ERROR, ResponseVo.MSG_ERROR, null);
    }

    public static ResponseVo getErrorResult(String code, String MSG) {
        return new ResponseVo(code, MSG, null);
    }

    public static ResponseVo getErrorResult(String code, String MSG,
                                            Object result) {
        return new ResponseVo(code, MSG, result);
    }

    public static ResponseVo getTokenErrorResult(String MSG) {
        return new ResponseVo(ResponseVo.CODE_TOKEN_ERROR, MSG, null);
    }

    /**
     * 通用成功返回操作
     *
     * @param result 返回数据 可为null
     * @return
     */
    public static ResponseVo getSuccResult(Object result) {
        return new ResponseVo(ResponseVo.CODE_OK, ResponseVo.MSG_OK, result);
    }

    public static ResponseVo getSuccResult(String MSG) {
        return new ResponseVo(ResponseVo.CODE_OK, MSG, null);
    }

    public static ResponseVo getSuccResult(String MSG, Object result) {
        return new ResponseVo(ResponseVo.CODE_OK, MSG, result);
    }

    public static ResponseVo getSuccResult() {
        return new ResponseVo(ResponseVo.CODE_OK, ResponseVo.MSG_OK, null);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
