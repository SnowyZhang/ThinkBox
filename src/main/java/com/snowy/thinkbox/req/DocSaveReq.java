package com.snowy.thinkbox.req;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotNull;

public class DocSaveReq {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @NotNull(message = "【名称】不能为空")
    private String name;

    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "【电子书】不能为空")
    private Long ebookId;

    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "【父级目录】不能为空")
    private Long parentId;

    @NotNull(message = "【优先级】不能为空")
    private Integer priority;

    private Integer viewCount;

    private Integer voteCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getEbookId() {
        return ebookId;
    }

    public void setEbookId(Long ebookId) {
        this.ebookId = ebookId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", ebookId=").append(ebookId);
        sb.append(", parentId=").append(parentId);
        sb.append(", priority=").append(priority);
        sb.append(", viewCount=").append(viewCount);
        sb.append(", voteCount=").append(voteCount);
        sb.append("]");
        return sb.toString();
    }
}