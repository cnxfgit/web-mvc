package com.web.mvc.common;

/**
 * 通用的json返回体
 */
public class Result {

    private Integer code;
    private String msg;
    private Object data;

    public static Result ok(){
        Result result = new Result();
        result.setCode(200);
        return result;
    }

    public static Result ok(String msg){
        Result result = new Result();
        result.setCode(200);
        result.setMsg(msg);
        return result;
    }

    public static Result ok(String msg,Object obj){
        Result result = new Result();
        result.setCode(200);
        result.setMsg(msg);
        result.setData(obj);
        return result;
    }

    public static Result fail(){
        Result result = new Result();
        result.setCode(500);
        return result;
    }

    public static Result fail(String msg){
        Result result = new Result();
        result.setCode(500);
        result.setMsg(msg);
        return result;
    }

    public static Result fail(String msg,Object obj){
        Result result = new Result();
        result.setCode(500);
        result.setMsg(msg);
        result.setData(obj);
        return result;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
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
