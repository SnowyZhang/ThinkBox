package com.snowy.thinkbox.service;



import com.snowy.thinkbox.mapper.EbookSnapshotMapper;
import com.snowy.thinkbox.mapper.MyEbookSnapshotMapper;
import com.snowy.thinkbox.resp.DataResp;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EbookSnapshotService {
    @Resource
    private MyEbookSnapshotMapper myEbookSnapshotMapper;

    public void insertEbookSnapshotItem() {
        myEbookSnapshotMapper.insertEbookSnapshotItem();
    }

    public void updateCount() {
        myEbookSnapshotMapper.updateCount();
    }

    public void updateIncrease() {
        myEbookSnapshotMapper.updateIncrease();
    }

    //一次进行多个数据库操作的情况
    public void SnapshotGenerate() {
        myEbookSnapshotMapper.SnapshotGenerate();

    }

    public List<DataResp> getData() {
        return myEbookSnapshotMapper.getData();
    }

    public List<DataResp> getMonthData() {
        return myEbookSnapshotMapper.getMonthData();
    }
}
