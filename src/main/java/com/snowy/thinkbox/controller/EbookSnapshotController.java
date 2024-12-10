package com.snowy.thinkbox.controller;

import com.snowy.thinkbox.domain.Demo;
import com.snowy.thinkbox.resp.CommonResp;
import com.snowy.thinkbox.resp.DataResp;
import com.snowy.thinkbox.service.DemoService;
import com.snowy.thinkbox.service.EbookSnapshotService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/snapshot")
public class EbookSnapshotController {
    @Resource
    private EbookSnapshotService ebookSnapshotService;


    @GetMapping("/get-data")
    public CommonResp<List<DataResp>> getData(){
        List<DataResp> list = ebookSnapshotService.getData();
        CommonResp<List<DataResp>> resp = new CommonResp<>();
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/get-month-data")
    public CommonResp<List<DataResp>> getMonthData(){
        List<DataResp> list = ebookSnapshotService.getMonthData();
        CommonResp<List<DataResp>> resp = new CommonResp<>();
        resp.setContent(list);
        return resp;
    }
}
