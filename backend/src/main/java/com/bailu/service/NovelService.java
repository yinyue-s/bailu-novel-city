package com.bailu.service;

import com.bailu.entity.Novel;
import com.bailu.mapper.NovelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NovelService {

    private final NovelMapper novelMapper;

    public NovelService(NovelMapper novelMapper) {
        this.novelMapper = novelMapper;
    }

    public List<Novel> searchNovels(String keyword) {
        return novelMapper.searchByKeyword(keyword);
    }

    public Novel findByNovelID(Integer novelID) {
        return novelMapper.findByNovelID(novelID);
    }

    public List<Novel> findByCategoryID(Integer categoryID) {
        return novelMapper.findByCategoryID(categoryID);
    }

    public List<Novel> findByAuthorID(Long authorID) {
        return novelMapper.findByAuthorID(authorID);
    }

    public List<Novel> findNovelsInBookshelf(Long userID) {
        return novelMapper.findNovelsInBookshelf(userID);
    }
}