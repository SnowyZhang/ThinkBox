package com.snowy.thinkbox.mapper;


import com.snowy.thinkbox.resp.DataResp;

import java.util.List;

public interface MyEbookSnapshotMapper {
    public void insertEbookSnapshotItem();
    public void updateCount();
    public void updateIncrease();

    public void SnapshotGenerate();

    public List<DataResp> getData();

    public List<DataResp> getMonthData();
}
