package com.snowy.thinkbox.mapper;

import com.snowy.thinkbox.domain.Content;
import com.snowy.thinkbox.domain.ContentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ContentMapper {
    long countByExample(ContentExample example);

    int deleteByExample(ContentExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Content row);

    int insertSelective(Content row);

    List<Content> selectByExampleWithBLOBs(ContentExample example);

    List<Content> selectByExample(ContentExample example);

    Content selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") Content row, @Param("example") ContentExample example);

    int updateByExampleWithBLOBs(@Param("row") Content row, @Param("example") ContentExample example);

    int updateByExample(@Param("row") Content row, @Param("example") ContentExample example);

    int updateByPrimaryKeySelective(Content row);

    int updateByPrimaryKeyWithBLOBs(Content row);
}