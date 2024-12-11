package com.snowy.thinkbox.exception;

public enum BusinessExceptionCode {
    VERYFY_TOKEN_ERROR("邮箱验证码无效"),
    VERYFY_TOKEN_EXPIRED("邮箱验证码已过期"),
    VERYFY_USER_NOT_FOUND("所验证的用户不存在"),
    USER_LOGIN_NAME_EXIST("登录名已存在"),
    LOGIN_USER_ERROR("用户名不存在或密码错误"),
    VOTE_REPEAT("您已点赞过"),
    ;

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
