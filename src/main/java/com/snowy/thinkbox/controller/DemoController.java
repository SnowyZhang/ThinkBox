package com.snowy.thinkbox.controller;

import com.snowy.thinkbox.domain.Demo;
import com.snowy.thinkbox.service.DemoService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/demo")
public class DemoController {
    @Resource
    private DemoService demoService;


    @GetMapping("/list")
    public List<Demo> List(){
        return demoService.list();
    }
}
