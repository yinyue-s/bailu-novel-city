package com.bailu.service;

import com.bailu.entity.Bookshelf;
import com.bailu.entity.BookshelfGroup;
import com.bailu.entity.VO.BookshelfNovel;
import com.bailu.mapper.BookshelfMapper;
import com.bailu.mapper.BookshelfGroupMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookshelfService {

    private final BookshelfMapper bookshelfMapper;
    private final BookshelfGroupMapper groupMapper;

    public BookshelfService(BookshelfMapper bookshelfMapper, BookshelfGroupMapper groupMapper) {
        this.bookshelfMapper = bookshelfMapper;
        this.groupMapper = groupMapper;
    }

    /**
     * 检查书籍是否已在书架中
     */
    public boolean existsInBookshelf(Long userID, Integer novelID) {
        return bookshelfMapper.findByUserIDAndNovelID(userID, novelID).isPresent();
    }

    /**
     * 添加书籍到书架 - 改进的默认分组逻辑
     */
    public boolean addToBookshelf(Long userID, Integer novelID, Integer groupID) {
        // 检查是否已存在
        Optional<Bookshelf> existing = bookshelfMapper.findByUserIDAndNovelID(userID, novelID);
        if (existing.isPresent()) {
            return false; // 已存在
        }

        Integer finalGroupID = groupID;

        // 如果groupID为null，查找或创建默认分组
        if (groupID == null) {
            try {
                // 首先尝试查找名为"默认分组"的分组
                Optional<BookshelfGroup> defaultGroup = groupMapper.findByUserIDAndGroupName(userID, "默认分组");

                if (defaultGroup.isPresent()) {
                    finalGroupID = defaultGroup.get().getGroupID();
                } else {
                    // 如果没有找到默认分组，查找用户的第一个分组
                    BookshelfGroup firstGroup = groupMapper.findFirstByUserID(userID);
                    if (firstGroup != null) {
                        finalGroupID = firstGroup.getGroupID();
                    } else {
                        // 如果用户没有任何分组，创建默认分组
                        BookshelfGroup newDefaultGroup = new BookshelfGroup();
                        newDefaultGroup.setUserID(userID);
                        newDefaultGroup.setGroupName("默认分组");
                        newDefaultGroup.setSortOrder(0);

                        int result = groupMapper.insert(newDefaultGroup);
                        if (result > 0) {
                            finalGroupID = newDefaultGroup.getGroupID();
                        } else {
                            // 如果创建失败，使用默认值1
                            finalGroupID = 1;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 出错时使用默认值
                finalGroupID = 1;
            }
        }

        Bookshelf bookshelf = new Bookshelf();
        bookshelf.setUserID(userID);
        bookshelf.setNovelID(novelID);
        bookshelf.setGroupID(finalGroupID);
        bookshelf.setIsPinned(false);

        return bookshelfMapper.insert(bookshelf) > 0;
    }

    /**
     * 书架显示书籍信息
     * @param userId
     * @return
     */
    public List<BookshelfNovel> getBookshelfListWithNovel(Long userId) {
        return bookshelfMapper.selectByUserWithNovel(userId);
    }

    /**
     * 从书架中移除书籍
     */
    public boolean removeFromBookshelf(Long userID, Integer shelfID) {
        return bookshelfMapper.deleteByShelfIDAndUserID(shelfID, userID) > 0;
    }

    /**
     * 将书籍移动到指定分组
     */
    public boolean moveToGroup(Long userID, Integer shelfID, Integer groupID) {
        return bookshelfMapper.updateGroup(shelfID, userID, groupID) > 0;
    }

    /**
     * 切换书籍的置顶状态
     */
    public boolean togglePinStatus(Long userID, Integer shelfID, Boolean isPinned) {
        return bookshelfMapper.updatePinnedStatus(shelfID, userID, isPinned) > 0;
    }

    /**
     * 更新最后阅读章节
     */
    public boolean updateLastRead(Long userID, Integer shelfID, Integer lastReadChapterID) {
        return bookshelfMapper.updateLastRead(shelfID, userID, lastReadChapterID) > 0;
    }

    /**
     * 获取用户的所有书架项
     */
    public List<Bookshelf> getUserBookshelf(Long userID) {
        return bookshelfMapper.findByUserID(userID);
    }

    /**
     * 获取特定的书架项
     */
    public Optional<Bookshelf> getBookshelfItem(Long userID, Integer shelfID) {
        return bookshelfMapper.findByShelfIDAndUserID(shelfID, userID);
    }

    /**
     * 获取用户的所有分组
     */
    public List<BookshelfGroup> getUserGroups(Long userID) {
        return groupMapper.findByUserID(userID);
    }

    /**
     * 创建新分组
     */
    public boolean createGroup(Long userID, String groupName) {
        BookshelfGroup group = new BookshelfGroup();
        group.setUserID(userID);
        group.setGroupName(groupName);
        group.setSortOrder(groupMapper.getNextSortOrder(userID));

        return groupMapper.insert(group) > 0;
    }

    /**
     * 更新分组名称
     */
    public boolean updateGroupName(Long userID, Integer groupID, String groupName) {
        return groupMapper.updateGroupName(groupID, userID, groupName) > 0;
    }

    /**
     * 删除分组(仅当分组为空）
     */
    public boolean deleteGroup(Long userID, Integer groupID) {
        // 先检查分组中是否有书籍
        List<Bookshelf> booksInGroup = bookshelfMapper.findByUserIDAndGroupID(userID, groupID);

        if (!booksInGroup.isEmpty()) {
            // 分组不为空，不能删除
            return false;
        }

        return groupMapper.deleteByGroupIDAndUserID(groupID, userID) > 0;
    }

    /**
     * 根据分组ID获取该分组下的所有书架项
     */
    public List<Bookshelf> getBookshelfByGroup(Long userID, Integer groupID) {
        return bookshelfMapper.findByUserIDAndGroupID(userID, groupID);
    }

    /**
     * 获取用户的所有置顶书籍
     */
    public List<Bookshelf> getPinnedBooks(Long userID) {
        return bookshelfMapper.findPinnedByUserID(userID);
    }

    /**
     * 获取用户最近阅读的书籍
     */
    public List<Bookshelf> getRecentReadBooks(Long userID, int limit) {
        return bookshelfMapper.findRecentByUserID(userID, limit);
    }
}