package com.snowy.thinkbox.domain;

import java.util.Date;

public class EbookSnapshot {
    private Long id;

    private Long ebookId;

    private Date date;

    private Integer viewCount;

    private Integer voteCount;

    private Integer viewCountIncrease;

    private Integer voteCountIncrease;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEbookId() {
        return ebookId;
    }

    public void setEbookId(Long ebookId) {
        this.ebookId = ebookId;
    }

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
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", ebookId=").append(ebookId);
        sb.append(", date=").append(date);
        sb.append(", viewCount=").append(viewCount);
        sb.append(", voteCount=").append(voteCount);
        sb.append(", viewCountIncrease=").append(viewCountIncrease);
        sb.append(", voteCountIncrease=").append(voteCountIncrease);
        sb.append("]");
        return sb.toString();
    }
}