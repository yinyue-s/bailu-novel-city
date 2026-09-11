package com.bailu.mapper;

import com.bailu.entity.Novel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NovelMapper {

    //模糊搜索
    @Select("SELECT * FROM novel WHERE novelName LIKE CONCAT('%', #{keyword}, '%')")
    List<Novel> searchByKeyword(@Param("keyword") String keyword);

    //根据id查询详细信息
    @Select("SELECT * FROM novel WHERE novelID = #{novelID}")
    Novel findByNovelID(@Param("novelID") Integer novelID);

    @Select("SELECT * FROM novel WHERE categoryID = #{categoryID}")
    List<Novel> findByCategoryID(@Param("categoryID") Integer categoryID);

    @Select("SELECT * FROM novel WHERE authorID = #{authorID}")
    List<Novel> findByAuthorID(@Param("authorID") Long authorID);

    @Select("SELECT n.* FROM novel n " +
            "INNER JOIN bookshelf b ON n.novelID = b.novelID " +
            "WHERE b.userID = #{userID} ORDER BY b.addTime DESC")
    List<Novel> findNovelsInBookshelf(@Param("userID") Long userID);
}