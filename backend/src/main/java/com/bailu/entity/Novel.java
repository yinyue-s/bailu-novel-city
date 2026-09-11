package com.bailu.entity;

import java.util.Date;
import java.time.LocalDateTime;

public class Novel {
    private Integer novelID;
    private String novelName;
    private Long authorID;
    private Integer categoryID;
    private String introduction;
    private String coverUrl;
    private String status;
    private Integer wordCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // getter和setter
    public Integer getNovelID() { return novelID; }
    public void setNovelID(Integer novelID) { this.novelID = novelID; }

    public String getNovelName() { return novelName; }
    public void setNovelName(String novelName) { this.novelName = novelName; }

    public Long getAuthorID() { return authorID; }
    public void setAuthorID(Long authorID) { this.authorID = authorID; }

    public Integer getCategoryID() { return categoryID; }
    public void setCategoryID(Integer categoryID) { this.categoryID = categoryID; }

    public String getIntroduction() { return introduction; }
    public void setIntroduction(String introduction) { this.introduction = introduction; }

    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getWordCount() { return wordCount; }
    public void setWordCount(Integer wordCount) { this.wordCount = wordCount; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}