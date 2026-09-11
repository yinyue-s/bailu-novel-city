package com.bailu.entity;

import java.time.LocalDateTime;

public class Bookshelf {
    private Integer shelfID;
    private Long userID;
    private Integer novelID;
    private Integer groupID;
    private LocalDateTime addTime;
    private Integer lastReadChapterID;  // 修正字段名
    private LocalDateTime lastReadTime;  // 修正字段名
    private Boolean isPinned;

    // getter和setter
    public Integer getShelfID() { return shelfID; }
    public void setShelfID(Integer shelfID) { this.shelfID = shelfID; }

    public Long getUserID() { return userID; }
    public void setUserID(Long userID) { this.userID = userID; }

    public Integer getNovelID() { return novelID; }
    public void setNovelID(Integer novelID) { this.novelID = novelID; }

    public Integer getGroupID() { return groupID; }
    public void setGroupID(Integer groupID) { this.groupID = groupID; }

    public LocalDateTime getAddTime() { return addTime; }
    public void setAddTime(LocalDateTime addTime) { this.addTime = addTime; }

    // 修正字段名
    public Integer getLastReadChapterID() { return lastReadChapterID; }
    public void setLastReadChapterID(Integer lastReadChapterID) { this.lastReadChapterID = lastReadChapterID; }

    // 修正字段名
    public LocalDateTime getLastReadTime() { return lastReadTime; }
    public void setLastReadTime(LocalDateTime lastReadTime) { this.lastReadTime = lastReadTime; }

    public Boolean getIsPinned() { return isPinned; }
    public void setIsPinned(Boolean isPinned) { this.isPinned = isPinned; }
}