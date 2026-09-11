package com.bailu.mapper;

import com.bailu.entity.BookshelfGroup;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BookshelfGroupMapper {


    @Select("SELECT * FROM bookshelfgroup WHERE userID = #{userID} ORDER BY sortOrder ASC LIMIT 1")
    BookshelfGroup findFirstByUserID(@Param("userID") Long userID);

    @Insert("INSERT INTO bookshelfgroup (userID, groupName, sortOrder) VALUES (#{userID}, #{groupName}, #{sortOrder})")
    @Options(useGeneratedKeys = true, keyProperty = "groupID")
    int insert(BookshelfGroup group);


    @Select("SELECT * FROM bookshelfgroup WHERE userID = #{userID} ORDER BY sortOrder")
    List<BookshelfGroup> findByUserID(@Param("userID") Long userID);


    @Select("SELECT * FROM bookshelfgroup WHERE groupID = #{groupID} AND userID = #{userID}")
    Optional<BookshelfGroup> findByGroupIDAndUserID(@Param("groupID") Integer groupID, @Param("userID") Long userID);

    // 通过用户ID和分组名查找分组
    @Select("SELECT * FROM bookshelfgroup WHERE userID = #{userID} AND groupName = #{groupName}")
    Optional<BookshelfGroup> findByUserIDAndGroupName(@Param("userID") Long userID, @Param("groupName") String groupName);


    @Update("UPDATE bookshelfgroup SET groupName = #{groupName} WHERE groupID = #{groupID} AND userID = #{userID}")
    int updateGroupName(@Param("groupID") Integer groupID, @Param("userID") Long userID, @Param("groupName") String groupName);


    @Update("UPDATE bookshelfgroup SET sortOrder = #{sortOrder} WHERE groupID = #{groupID} AND userID = #{userID}")
    int updateSortOrder(@Param("groupID") Integer groupID, @Param("userID") Long userID, @Param("sortOrder") Integer sortOrder);


    @Delete("DELETE FROM bookshelfgroup WHERE groupID = #{groupID} AND userID = #{userID}")
    int deleteByGroupIDAndUserID(@Param("groupID") Integer groupID, @Param("userID") Long userID);


    @Select("SELECT COALESCE(MAX(sortOrder), 0) + 1 FROM bookshelfgroup WHERE userID = #{userID}")
    Integer getNextSortOrder(@Param("userID") Long userID);
}