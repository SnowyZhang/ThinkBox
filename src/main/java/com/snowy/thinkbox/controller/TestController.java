package com.snowy.thinkbox.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @RequestMapping("/hello")
    public String hello(){
        return "Hello World!";
    }
    @PostMapping("/hello/post")
    public String postHello(String name){
        return "Hello World!"+name;
    }
}
