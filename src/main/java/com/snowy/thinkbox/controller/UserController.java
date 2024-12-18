package com.snowy.thinkbox.controller;

import com.alibaba.fastjson.JSONObject;
import com.snowy.thinkbox.req.*;
import com.snowy.thinkbox.resp.CommonResp;
import com.snowy.thinkbox.resp.PageResp;
import com.snowy.thinkbox.resp.UserLoginResp;
import com.snowy.thinkbox.resp.UserQueryResp;
import com.snowy.thinkbox.service.UserService;
import com.snowy.thinkbox.utils.SnowFlake;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private SnowFlake snowFlake;

    private static final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);


    @GetMapping("/list")
    public CommonResp List(@Valid UserQueryReq userQueryReq){
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        PageResp<UserQueryResp> list = userService.list(userQueryReq);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody @Valid UserSaveReq req){  //RequestBody对应前端传来的json数据;如果是form表单提交的数据，用@RequestParam
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes())); // 对密码进行MD5加密
        CommonResp resp = new CommonResp<>();
        userService.save(req);
        return resp;
    }

    @PostMapping("/register")
    public CommonResp save(@RequestBody @Valid UserRegisterReq req){  //RequestBody对应前端传来的json数据;如果是form表单提交的数据，用@RequestParam
        CommonResp resp = new CommonResp<>();
        userService.register(req);
        return resp;
    }

    @PostMapping("/verify-email/{token}")
    public CommonResp veryfyEmail(@PathVariable String token){
        CommonResp resp = new CommonResp<>();
        userService.veryfyEmail(token);
        return resp;
    }

    @PostMapping("/reset-password")
    public CommonResp resetPassword(@RequestBody @Valid UserResetPasswordReq req){  //RequestBody对应前端传来的json数据;如果是form表单提交的数据，用@RequestParam
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes())); // 对密码进行MD5加密
        CommonResp resp = new CommonResp<>();
        userService.resetPassword(req);
        return resp;
    }
    @PostMapping("/login")
    public CommonResp login(@RequestBody @Valid UserLoginReq req){  //RequestBody对应前端传来的json数据;如果是form表单提交的数据，用@RequestParam
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes())); // 对密码进行MD5加密
        CommonResp<UserLoginResp> resp = new CommonResp<>();
        UserLoginResp userLoginResp = userService.login(req);
        Long token =  snowFlake.nextId();
        userLoginResp.setToken(token.toString());
        redisTemplate.opsForValue().set(token.toString(), JSONObject.toJSONString(userLoginResp), 3600, TimeUnit.SECONDS); // 将登录信息存入Redis,有效期为1小时
        log.info("User logged in successfully, token: {}", token);
        resp.setContent(userLoginResp);
        return resp;
    }

    /*
     * 在SpringMVC中,@PathVariable可以用来映射URL中的占位符到目标方法的参数中.大括号用于声明占位符,占位符的名称必顫和@RequestMapping中的名称一致.
     * 而在Spring的其他地方,比如Spring EL表达式中,占位符是用${}来声明的,用于表示引用变量或表达式
     */
    @PostMapping("/delete/{id}") // 或使用 @DeleteMapping
    public CommonResp delete(@PathVariable String id) { //RequestBody对应前端传来的json数据;如果是form表单提交的数据，用@RequestParam
        CommonResp resp = new CommonResp<>();
        try {
            Long longId = Long.parseLong(id); // 尝试将字符串转为 Long
            userService.delete(longId);
        } catch (NumberFormatException e) {
            resp.setSuccess(false);
            resp.setMessage("Invalid ID format: " + id);
            return resp;
        }

        resp.setSuccess(true);
        resp.setMessage("Delete operation successful for ID: " + id);
        return resp;
    }

    @GetMapping("/logout/{token}") // 或使用 @DeleteMapping
    public CommonResp logout(@PathVariable String token) {
        CommonResp resp = new CommonResp<>();
        redisTemplate.delete(token);
        log.info("User logged out successfully, token: {}", token);
        return resp;
    }


}
