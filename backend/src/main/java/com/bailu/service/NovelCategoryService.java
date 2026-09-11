package com.bailu.service;

import com.bailu.entity.NovelCategory;
import com.bailu.mapper.NovelCategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 小说类别业务逻辑
 */
@Service
public class NovelCategoryService {

    private final NovelCategoryMapper novelCategoryMapper;

    public NovelCategoryService(NovelCategoryMapper novelCategoryMapper) {
        this.novelCategoryMapper = novelCategoryMapper;
    }

    /**
     * 获取全部小说分类
     */
    public List<NovelCategory> listAll() {
        return novelCategoryMapper.findAll();
    }
}