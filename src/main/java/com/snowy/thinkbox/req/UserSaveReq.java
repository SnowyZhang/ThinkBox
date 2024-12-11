package com.snowy.thinkbox.req;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public class UserSaveReq {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @NotNull(message = "Email cannot be empty")
    private String email;

    @NotNull(message = "Role cannot be empty")
    private String role;

    @NotNull(message = "isActive cannot be empty")
    private  Integer isActive;

    @NotNull(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Password cannot be empty")
    @Length(min = 6, max = 32, message = "Password length must be between 6 and 32 characters")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$", message = "Password must contain both letters and numbers")
    private String password;

    @Override
    public String toString() {
        return "UserSaveReq{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", isActive=" + isActive +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull(message = "Email cannot be empty") String getEmail() {
        return email;
    }

    public void setEmail(@NotNull(message = "Email cannot be empty") String email) {
        this.email = email;
    }

    public @NotNull(message = "Role cannot be empty") String getRole() {
        return role;
    }

    public void setRole(@NotNull(message = "Role cannot be empty") String role) {
        this.role = role;
    }

    public @NotNull(message = "isActive cannot be empty") Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(@NotNull(message = "isActive cannot be empty") Integer isActive) {
        this.isActive = isActive;
    }

    public @NotNull(message = "Name cannot be empty") String getName() {
        return name;
    }

    public void setName(@NotNull(message = "Name cannot be empty") String name) {
        this.name = name;
    }

    public @NotNull(message = "Password cannot be empty") @Length(min = 6, max = 32, message = "Password length must be between 6 and 32 characters") @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$", message = "Password must contain both letters and numbers") String getPassword() {
        return password;
    }

    public void setPassword(@NotNull(message = "Password cannot be empty") @Length(min = 6, max = 32, message = "Password length must be between 6 and 32 characters") @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$", message = "Password must contain both letters and numbers") String password) {
        this.password = password;
    }
}