package com.web.mvc.testwork.common;

/**
 * 适应Layui 表格的返回体
 */
public class TableResult {

    private Integer code;
    private Integer count;
    private Object data;
    private String msg;

    public static TableResult ok(Object object,Integer count){
        TableResult result = new TableResult();
        result.setCode(0);
        result.setData(object);
        result.setCount(count);
        result.setMsg("ok");
        return result;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
