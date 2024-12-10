package com.snowy.thinkbox.resp;

import java.util.Date;

public class DataResp {

    private Date date;

    private Integer viewCount;

    private Integer voteCount;

    private Integer viewCountIncrease;

    private Integer voteCountIncrease;


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Integer getViewCountIncrease() {
        return viewCountIncrease;
    }

    public void setViewCountIncrease(Integer viewCountIncrease) {
        this.viewCountIncrease = viewCountIncrease;
    }

    public Integer getVoteCountIncrease() {
        return voteCountIncrease;
    }

    public void setVoteCountIncrease(Integer voteCountIncrease) {
        this.voteCountIncrease = voteCountIncrease;
    }

    @Override
    public String toString() {
        return "DataResp{" +
                "date=" + date +
                ", viewCount=" + viewCount +
                ", voteCount=" + voteCount +
                ", viewCountIncrease=" + viewCountIncrease +
                ", voteCountIncrease=" + voteCountIncrease +
                '}';
    }
}