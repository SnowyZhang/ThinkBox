package com.snowy.thinkbox.controller;

import com.snowy.thinkbox.domain.Ebook;
import com.snowy.thinkbox.service.EbookService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EbookController {
    @Resource
    private EbookService ebookService;


    @GetMapping("/list")
    public List<Ebook> List(){
        return ebookService.list();
    }
}
