package com.yqy.wiki.exception;

/**
 * @author bahsk
 * @createTime 2021-07-26 17:08
 * @description 用于自定义的业务异常
 */
public enum BusinessExceptionCode {

    USER_LOGIN_NAME_EXIST("登录名已存在"),
    VOTE_REPEAT("您已点赞过"),
    LOGIN_USER_ERROR("用户名不存在或者密码错误");

    private String desc;

    BusinessExceptionCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
