package com.snowy.thinkbox.controller;

import com.snowy.thinkbox.domain.Test;
import com.snowy.thinkbox.service.TestService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    @Resource
    private TestService testService;


    @RequestMapping("/hello")
    public String hello(){
        return "Hello World!";
    }

    @PostMapping("/hello/post")
    public String postHello(String name){
        return "Hello World!"+name;
    }

    @GetMapping("/test/list")
    public List<Test> List(){
        return testService.list();
    }
}
