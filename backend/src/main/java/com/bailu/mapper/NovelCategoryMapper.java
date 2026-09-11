package com.bailu.mapper;


import com.bailu.entity.NovelCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 小说类别 Mapper
 */
@Mapper
public interface NovelCategoryMapper {

    /**
     * 查询所有小说类别
     */
    @Select("SELECT categoryID, categoryName " +
            "FROM NovelCategory ORDER BY categoryID")
    List<NovelCategory> findAll();
}
