package com.snowy.thinkbox.controller;

import com.snowy.thinkbox.req.CategoryQueryReq;
import com.snowy.thinkbox.req.CategorySaveReq;
import com.snowy.thinkbox.resp.CommonResp;
import com.snowy.thinkbox.resp.CategoryQueryResp;
import com.snowy.thinkbox.resp.PageResp;
import com.snowy.thinkbox.service.CategoryService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;


    @GetMapping("/list")
    public CommonResp List(@Valid CategoryQueryReq categoryQueryReq){
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        PageResp<CategoryQueryResp> list = categoryService.list(categoryQueryReq);
        resp.setContent(list);
        return resp;
    }
    @GetMapping("/all")
    public CommonResp all(@Valid CategoryQueryReq categoryQueryReq){
        CommonResp<List<CategoryQueryResp>> resp = new CommonResp<>();
        List<CategoryQueryResp> list = categoryService.all(categoryQueryReq);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody @Valid CategorySaveReq req){  //RequestBody对应前端传来的json数据;如果是form表单提交的数据，用@RequestParam
        CommonResp resp = new CommonResp<>();
        categoryService.save(req);
        return resp;
    }

    /*
     * 在SpringMVC中,@PathVariable可以用来映射URL中的占位符到目标方法的参数中.大括号用于声明占位符,占位符的名称必顫和@RequestMapping中的名称一致.
     * 而在Spring的其他地方,比如Spring EL表达式中,占位符是用${}来声明的,用于表示引用变量或表达式
     */
    @PostMapping("/delete/{id}") //Restful风格
    public CommonResp delete(@PathVariable Long id){  //RequestBody对应前端传来的json数据;如果是form表单提交的数据，用@RequestParam
        CommonResp resp = new CommonResp<>();
        categoryService.delete(id);
        return resp;
    }

}
