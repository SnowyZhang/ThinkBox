package com.snowy.thinkbox.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public class UserLoginResp {
    private String token;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @NotNull(message = "Login name cannot be empty")
    private String loginName;

    @NotNull(message = "Name cannot be empty")
    private String name;

    @Override
    public String toString() {
        return "UserLoginResp{" +
                "token=" + token +
                ", id=" + id +
                ", loginName='" + loginName + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @JsonSerialize(using = ToStringSerializer.class)
    public Long getId() {
        return id;
    }

    @JsonSerialize(using = ToStringSerializer.class)
    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}