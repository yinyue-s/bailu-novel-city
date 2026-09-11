package com.bailu.controller;

import com.bailu.entity.Novel;
import com.bailu.service.NovelService;
import com.bailu.util.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/novels")
@CrossOrigin(origins = "*")
public class NovelController {

    private final NovelService novelService;
    private final JwtTokenProvider tokenProvider;

    public NovelController(NovelService novelService, JwtTokenProvider tokenProvider) {
        this.novelService = novelService;
        this.tokenProvider = tokenProvider;
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchNovels(
            @RequestParam String keyword,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        try {
            // 修改1：添加调试日志
            System.out.println("收到搜索请求，keyword: " + keyword);
            System.out.println("Authorization头: " + authHeader);

            // 修改2：完善空值判断
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                System.out.println("Token格式错误或缺失");
                return ResponseEntity.status(401).body("未提供Token或Token格式错误");
            }

            String token = authHeader.replace("Bearer ", "");
            if (token.isEmpty()) {
                System.out.println("Token为空");
                return ResponseEntity.status(401).body("未提供Token");
            }

            // 修改3：添加详细日志
            System.out.println("提取的Token: " + token.substring(0, Math.min(token.length(), 50)) + "...");

            if (!tokenProvider.validateToken(token)) {
                System.out.println("Token验证失败");
                return ResponseEntity.status(401).body("Token无效或已过期");
            }

            System.out.println("Token验证成功，开始搜索");

            // 修改4：参数验证
            if (keyword == null || keyword.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("搜索关键词不能为空");
            }

            // 修改5：防止SQL注入
            String safeKeyword = keyword.trim()
                    .replaceAll("[<>\"']", "") // 移除危险字符
                    .replaceAll("\\s+", " ");  // 合并多个空格

            if (safeKeyword.length() > 100) {
                safeKeyword = safeKeyword.substring(0, 100); // 限制长度
            }

            // 修改6：执行搜索并返回结果
            List<Novel> novels = novelService.searchNovels(safeKeyword);
            System.out.println("搜索完成，结果数量: " + novels.size());

            // 返回统一格式的响应
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", novels);
            response.put("total", novels.size());
            response.put("keyword", safeKeyword);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            System.out.println("参数错误: " + e.getMessage());
            return ResponseEntity.badRequest().body("请求参数错误: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("搜索异常: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("服务器错误，请稍后重试");
        }
    }

    // 其他方法保持不变
    @GetMapping("/{novelID}")
    public ResponseEntity<?> getNovelById(
            @PathVariable Integer novelID,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("未登录或Token无效");
        }

        String token = authHeader.substring(7);
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("未登录或Token无效");
        }

        Novel novel = novelService.findByNovelID(novelID);
        return ResponseEntity.ok(novel);
    }

    @GetMapping("/category/{categoryID}")
    public ResponseEntity<?> getNovelsByCategory(
            @PathVariable Integer categoryID,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("未登录或Token无效");
        }

        String token = authHeader.substring(7);
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("未登录或Token无效");
        }

        return ResponseEntity.ok(novelService.findByCategoryID(categoryID));
    }

    @GetMapping("/author/{authorID}")
    public ResponseEntity<?> getNovelsByAuthor(
            @PathVariable Long authorID,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("未登录或Token无效");
        }

        String token = authHeader.substring(7);
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("未登录或Token无效");
        }

        return ResponseEntity.ok(novelService.findByAuthorID(authorID));
    }
}