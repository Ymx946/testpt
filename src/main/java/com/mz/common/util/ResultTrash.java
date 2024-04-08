package com.mz.common.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public final class ResultTrash implements Serializable {
    private static final long serialVersionUID = 879037396349251680L;

    private int errcode;

    private String errmsg;

    private Object data = "";

    public ResultTrash(int errcode, String errmsg) {
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public static ResultTrash instance(int errcode, String errmsg) {
        return new ResultTrash(errcode, errmsg);
    }

    public static ResultTrash instance(int errcode, String errmsg, Object data) {
        return new ResultTrash(errcode, errmsg, data);
    }

    public static ResultTrash success() {
        return new ResultTrash(ResponseCodeTrash.SUCCESS.getErrCode(), ResponseCodeTrash.SUCCESS.getErrMsg());
    }

    public static ResultTrash success(Object data) {
        return new ResultTrash(ResponseCodeTrash.SUCCESS.getErrCode(), ResponseCodeTrash.SUCCESS.getErrMsg(), data);
    }

    public static ResultTrash failed() {
        return new ResultTrash(ResponseCodeTrash.FAILED.getErrCode(), ResponseCodeTrash.FAILED.getErrMsg());
    }

    public static ResultTrash failedLogin() {
        return new ResultTrash(ResponseCodeTrash.FAILEDLOGIN.getErrCode(), ResponseCodeTrash.FAILEDLOGIN.getErrMsg());
    }

    public static ResultTrash failed(Object data) {
        return new ResultTrash(ResponseCodeTrash.FAILED.getErrCode(), ResponseCodeTrash.FAILED.getErrMsg() + ", " + data, data);
    }

    public static ResultTrash warning(Object data) {
        return new ResultTrash(ResponseCodeTrash.WARNING.getErrCode(), String.valueOf(data), data);
    }

    public static ResultTrash failedLogin(Object data) {
        return new ResultTrash(ResponseCodeTrash.FAILEDLOGIN.getErrCode(), ResponseCodeTrash.FAILED.getErrMsg() + ", " + data, data);
    }

    public static ResultTrash errorPassword() {
        return new ResultTrash(ResponseCodeTrash.ERRORPASSWORD.getErrCode(), ResponseCodeTrash.ERRORPASSWORD.getErrMsg());
    }

    public static ResultTrash userExits() {
        return new ResultTrash(ResponseCodeTrash.USER_LOGIN_EXITS.getErrCode(), ResponseCodeTrash.USER_LOGIN_EXITS.getErrMsg());
    }

    @Override
    public String toString() {
        return "ResultTrash{" + "errcode=" + errcode + ", errmsg='" + errmsg + '\'' + ", data=" + data + '}';
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
