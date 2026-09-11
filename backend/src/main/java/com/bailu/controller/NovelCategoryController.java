package com.bailu.controller;

import com.bailu.entity.NovelCategory;
import com.bailu.service.NovelCategoryService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 小说类别相关接口
 */
@RestController
@CrossOrigin(origins = "*")
public class NovelCategoryController {

    private final NovelCategoryService novelCategoryService;

    public NovelCategoryController(NovelCategoryService novelCategoryService) {
        this.novelCategoryService = novelCategoryService;
    }

    /**
     * 首页“小说分类”使用的接口
     * GET /api/categories
     */
    @GetMapping("/api/categories")
    public List<NovelCategory> listAllCategories() {
        return novelCategoryService.listAll();
    }
}
