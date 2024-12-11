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
    private String email;

    @NotNull(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Role cannot be empty")
    private String role;

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

    public @NotNull(message = "Login name cannot be empty") String getEmail() {
        return email;
    }

    public void setEmail(@NotNull(message = "Login name cannot be empty") String email) {
        this.email = email;
    }

    public @NotNull(message = "Role cannot be empty") String getRole() {
        return role;
    }

    public void setRole(@NotNull(message = "Role cannot be empty") String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserLoginResp{" +
                "token='" + token + '\'' +
                ", id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}