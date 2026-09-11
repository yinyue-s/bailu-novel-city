package com.bailu.mapper;

import com.bailu.entity.Bookshelf;
import com.bailu.entity.VO.BookshelfNovel;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BookshelfMapper {

    @Insert("INSERT INTO bookshelf (userID, novelID, groupID, addTime, isPinned) " +
            "VALUES (#{userID}, #{novelID}, #{groupID}, NOW(), #{isPinned})")
    @Options(useGeneratedKeys = true, keyProperty = "shelfID")
    int insert(Bookshelf bookshelf);

    @Delete("DELETE FROM bookshelf WHERE shelfID = #{shelfID} AND userID = #{userID}")
    int deleteByShelfIDAndUserID(@Param("shelfID") Integer shelfID, @Param("userID") Long userID);

    @Update("UPDATE bookshelf SET groupID = #{groupID} WHERE shelfID = #{shelfID} AND userID = #{userID}")
    int updateGroup(@Param("shelfID") Integer shelfID, @Param("userID") Long userID, @Param("groupID") Integer groupID);

    @Update("UPDATE bookshelf SET isPinned = #{isPinned} WHERE shelfID = #{shelfID} AND userID = #{userID}")
    int updatePinnedStatus(@Param("shelfID") Integer shelfID, @Param("userID") Long userID, @Param("isPinned") Boolean isPinned);

    @Update("UPDATE bookshelf SET lastReadChapterID = #{lastReadChapterID}, lastReadTime = NOW() " +
            "WHERE shelfID = #{shelfID} AND userID = #{userID}")
    int updateLastRead(@Param("shelfID") Integer shelfID, @Param("userID") Long userID, @Param("lastReadChapterID") Integer lastReadChapterID);

    @Select("SELECT * FROM bookshelf WHERE userID = #{userID}")
    List<Bookshelf> findByUserID(@Param("userID") Long userID);

    @Select("SELECT * FROM bookshelf WHERE userID = #{userID} AND novelID = #{novelID}")
    Optional<Bookshelf> findByUserIDAndNovelID(@Param("userID") Long userID, @Param("novelID") Integer novelID);

    @Select("SELECT * FROM bookshelf WHERE shelfID = #{shelfID} AND userID = #{userID}")
    Optional<Bookshelf> findByShelfIDAndUserID(@Param("shelfID") Integer shelfID, @Param("userID") Long userID);

    @Select("SELECT * FROM bookshelf WHERE userID = #{userID} AND groupID = #{groupID}")
    List<Bookshelf> findByUserIDAndGroupID(@Param("userID") Long userID, @Param("groupID") Integer groupID);

    @Select("SELECT * FROM bookshelf WHERE userID = #{userID} AND isPinned = true ORDER BY addTime DESC")
    List<Bookshelf> findPinnedByUserID(@Param("userID") Long userID);

    // 修正字段名
    @Select("SELECT * FROM bookshelf WHERE userID = #{userID} ORDER BY lastReadTime DESC LIMIT #{limit}")
    List<Bookshelf> findRecentByUserID(@Param("userID") Long userID, @Param("limit") int limit);

    // 在 BookshelfMapper.java 中修改该方法的 SQL
    @Select("""
        SELECT 
            b.shelfID, b.groupID, b.novelID, b.addTime,
            n.novelName AS novelName,  -- 改为实际字段名 novelName
            n.introduction AS introduction,
            n.status AS status,
            n.wordCount AS wordCount  -- 改为实际字段名 wordCount
        FROM bookshelf b
        JOIN novel n ON b.novelID = n.novelID
        WHERE b.userID = #{userId}
        """)
    List<BookshelfNovel> selectByUserWithNovel(@Param("userId") Long userId);


}