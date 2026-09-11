package com.bailu.entity;

public class NovelCategory {
    private Integer categoryID;     // 类别ID
    private String categoryName;

    public NovelCategory() {}

    public NovelCategory(Integer categoryID, String categoryName) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
    }
    //setter getter
    public Integer getCategoryID(){
        return categoryID;
    }
    public void setCategoryID(Integer categoryID){
        this.categoryID = categoryID;
    }

    public String getCategoryName(){
        return categoryName;
    }
    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }
}
