package com.bailu.entity.VO;

import java.util.Date;

/**
 * 书架+小说的跨表视图对象（用于前端展示）
 */
import java.util.Date;

/**
 * 书架+小说 跨表实体类（用于接收关联查询结果）
 */
public class BookshelfNovel {
    // 来自bookshelf表的字段
    private Integer shelfID;       // 书架记录ID
    private Long userID;           // 用户ID
    private Integer novelID;       // 小说ID
    private Integer groupID;       // 分组ID
    private Date addTime;          // 加入书架时间
    private Integer lastReadChapterID; // 最后阅读章节ID
    private Date lastReadTime;     // 最后阅读时间
    private Boolean isPinned;      // 是否置顶

    // 来自novel表的字段
    private String novelName;      // 小说名称
    private String introduction;   // 小说简介
    private String status;         // 小说状态（连载中/已完结）
    private Long wordCount;        // 小说字数
    private String coverUrl;       // 小说封面URL

    // 无参构造方法（MyBatis/JSON反序列化必需）
    public BookshelfNovel() {
    }

    // 全参构造方法（方便手动创建对象）
    public BookshelfNovel(Integer shelfID, Long userID, Integer novelID, Integer groupID, Date addTime,
                          Integer lastReadChapterID, Date lastReadTime, Boolean isPinned,
                          String novelName, String introduction, String status, Long wordCount, String coverUrl) {
        this.shelfID = shelfID;
        this.userID = userID;
        this.novelID = novelID;
        this.groupID = groupID;
        this.addTime = addTime;
        this.lastReadChapterID = lastReadChapterID;
        this.lastReadTime = lastReadTime;
        this.isPinned = isPinned;
        this.novelName = novelName;
        this.introduction = introduction;
        this.status = status;
        this.wordCount = wordCount;
        this.coverUrl = coverUrl;
    }

    // ========== getter/setter 方法 ==========
    public Integer getShelfID() {
        return shelfID;
    }

    public void setShelfID(Integer shelfID) {
        this.shelfID = shelfID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Integer getNovelID() {
        return novelID;
    }

    public void setNovelID(Integer novelID) {
        this.novelID = novelID;
    }

    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getLastReadChapterID() {
        return lastReadChapterID;
    }

    public void setLastReadChapterID(Integer lastReadChapterID) {
        this.lastReadChapterID = lastReadChapterID;
    }

    public Date getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(Date lastReadTime) {
        this.lastReadTime = lastReadTime;
    }

    public Boolean getPinned() {
        return isPinned;
    }

    public void setPinned(Boolean pinned) {
        isPinned = pinned;
    }

    public String getNovelName() {
        return novelName;
    }

    public void setNovelName(String novelName) {
        this.novelName = novelName;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getWordCount() {
        return wordCount;
    }

    public void setWordCount(Long wordCount) {
        this.wordCount = wordCount;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    // ========== toString 方法（方便日志打印/调试） ==========
    @Override
    public String toString() {
        return "BookshelfNovel{" +
                "shelfID=" + shelfID +
                ", userID=" + userID +
                ", novelID=" + novelID +
                ", groupID=" + groupID +
                ", addTime=" + addTime +
                ", lastReadChapterID=" + lastReadChapterID +
                ", lastReadTime=" + lastReadTime +
                ", isPinned=" + isPinned +
                ", novelName='" + novelName + '\'' +
                ", introduction='" + introduction + '\'' +
                ", status='" + status + '\'' +
                ", wordCount=" + wordCount +
                ", coverUrl='" + coverUrl + '\'' +
                '}';
    }
}