package com.bailu.entity;

public class BookshelfGroup {
    private Integer groupID;
    private Long userID;
    private String groupName;
    private Integer sortOrder;

    // getter和setter
    public Integer getGroupID() { return groupID; }
    public void setGroupID(Integer groupID) { this.groupID = groupID; }

    public Long getUserID() { return userID; }
    public void setUserID(Long userID) { this.userID = userID; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}