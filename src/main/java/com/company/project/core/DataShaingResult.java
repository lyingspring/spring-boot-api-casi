package com.company.project.core;

import com.alibaba.fastjson.JSON;

public class DataShaingResult {
    private int result_code;
    private String msg;
    private Object data;

    public DataShaingResult() {

    }

    public DataShaingResult setResult_code(ResultCode resultCode) {
        this.result_code = resultCode.code();
        return this;
    }
    public  DataShaingResult(Result result){
        this.result_code = result.getCode();
        this.msg= result.getMessage();
        this.data= result.getData();
    }

    public int getResult_code() {
        return result_code;
    }

    public String getMsg() {
        return msg;
    }

    public DataShaingResult setMsg(String message) {
        this.msg = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public DataShaingResult setData(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
