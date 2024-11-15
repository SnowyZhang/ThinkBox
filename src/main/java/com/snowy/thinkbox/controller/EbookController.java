package com.snowy.thinkbox.controller;

import com.snowy.thinkbox.req.EbookQueryReq;
import com.snowy.thinkbox.req.EbookSaveReq;
import com.snowy.thinkbox.resp.CommonResp;
import com.snowy.thinkbox.resp.EbookQueryResp;
import com.snowy.thinkbox.resp.PageResp;
import com.snowy.thinkbox.service.EbookService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ebook")
public class EbookController {
    @Resource
    private EbookService ebookService;


    @GetMapping("/list")
    public CommonResp List(EbookQueryReq ebookQueryReq){
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(ebookQueryReq);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody EbookSaveReq req){  //RequestBody对应前端传来的json数据;如果是form表单提交的数据，用@RequestParam
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }

}
