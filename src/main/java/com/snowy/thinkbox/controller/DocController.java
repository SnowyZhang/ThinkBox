package com.snowy.thinkbox.controller;

import com.snowy.thinkbox.req.DocQueryReq;
import com.snowy.thinkbox.req.DocSaveReq;
import com.snowy.thinkbox.resp.DocQueryResp;
import com.snowy.thinkbox.resp.CommonResp;
import com.snowy.thinkbox.resp.PageResp;
import com.snowy.thinkbox.service.DocService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {
    @Resource
    private DocService docService;


    @GetMapping("/list")
    public CommonResp List(@Valid DocQueryReq docQueryReq){
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(docQueryReq);
        resp.setContent(list);
        return resp;
    }
    @GetMapping("/all")
    public CommonResp all(@Valid DocQueryReq docQueryReq){
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.all(docQueryReq);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody @Valid DocSaveReq req){  //RequestBody对应前端传来的json数据;如果是form表单提交的数据，用@RequestParam
        CommonResp resp = new CommonResp<>();
        docService.save(req);
        return resp;
    }

    /*
     * 在SpringMVC中,@PathVariable可以用来映射URL中的占位符到目标方法的参数中.大括号用于声明占位符,占位符的名称必顫和@RequestMapping中的名称一致.
     * 而在Spring的其他地方,比如Spring EL表达式中,占位符是用${}来声明的,用于表示引用变量或表达式
     */
    @PostMapping("/delete/{id}") //Restful风格
    public CommonResp delete(@PathVariable String id){  //RequestBody对应前端传来的json数据;如果是form表单提交的数据，用@RequestParam
        CommonResp resp = new CommonResp<>();
        try {
            Long longId = Long.parseLong(id); // 尝试将字符串转为 Long
            docService.delete(longId);
        } catch (NumberFormatException e) {
            resp.setSuccess(false);
            resp.setMessage("Invalid ID format: " + id);
            return resp;
        }

        resp.setSuccess(true);
        resp.setMessage("Delete operation successful for ID: " + id);
        return resp;
    }



}
