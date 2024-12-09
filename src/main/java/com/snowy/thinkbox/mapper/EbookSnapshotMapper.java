package com.snowy.thinkbox.mapper;

import com.snowy.thinkbox.domain.EbookSnapshot;
import com.snowy.thinkbox.domain.EbookSnapshotExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EbookSnapshotMapper {
    long countByExample(EbookSnapshotExample example);

    int deleteByExample(EbookSnapshotExample example);

    int deleteByPrimaryKey(Long id);

    int insert(EbookSnapshot row);

    int insertSelective(EbookSnapshot row);

    List<EbookSnapshot> selectByExample(EbookSnapshotExample example);

    EbookSnapshot selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") EbookSnapshot row, @Param("example") EbookSnapshotExample example);

    int updateByExample(@Param("row") EbookSnapshot row, @Param("example") EbookSnapshotExample example);

    int updateByPrimaryKeySelective(EbookSnapshot row);

    int updateByPrimaryKey(EbookSnapshot row);
}