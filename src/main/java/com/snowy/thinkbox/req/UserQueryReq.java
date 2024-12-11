package com.snowy.thinkbox.req;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class UserQueryReq extends PageReq{
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserQueryReq{" +
                "email='" + email + '\'' +
                "} " + super.toString();
    }
}