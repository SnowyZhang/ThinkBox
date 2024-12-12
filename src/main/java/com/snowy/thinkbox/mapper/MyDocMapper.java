package com.snowy.thinkbox.mapper;

import com.snowy.thinkbox.domain.Test;
import com.snowy.thinkbox.resp.CommentsQueryResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MyDocMapper {
    public void updateViewCount(@Param("id") Long id);
    public void updateVoteCount(@Param("id") Long id);
    public void updateEbookInfo();
    public List<CommentsQueryResp> findComment(@Param("docID") Long docId);

}
