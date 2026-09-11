package com.bailu.controller;

import com.bailu.entity.Bookshelf;
import com.bailu.entity.BookshelfGroup;
import com.bailu.entity.VO.BookshelfNovel;
import com.bailu.service.BookshelfService;
import com.bailu.service.UserService;
import com.bailu.util.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookshelf")
@CrossOrigin(origins = "*")
public class BookshelfController {

    private final BookshelfService bookshelfService;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    public BookshelfController(BookshelfService bookshelfService, JwtTokenProvider tokenProvider, UserService userService) {
        this.bookshelfService = bookshelfService;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    /**
     * 从Authorization头部中提取用户ID
     * @param authHeader 认证头信息
     * @return 用户ID，如果token无效则返回null
     */
    private Long getUserIdFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authHeader.substring(7);
        if (!tokenProvider.validateToken(token)) {
            return null;
        }
        String username = tokenProvider.getUsernameFromToken(token);
        // 根据username获取userID
        return userService.findUserIDByUsername(username);
    }

    /**
     * 添加书籍到书架
     * @param authHeader 认证头信息
     * @param request 包含novelID和可选groupID的请求体
     * @return 操作结果
     */
    @PostMapping("/add")
    public ResponseEntity<?> addToBookshelf(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> request) {
        try {
            Long userID = getUserIdFromToken(authHeader);
            if (userID == null) {
                return ResponseEntity.status(401).body(Map.of("success", false, "message", "未登录或Token无效"));
            }

            Integer novelID = null; // 修改为Integer
            Integer groupID = null;

            try {
                // 处理novelID，支持多种类型
                Object novelIdObj = request.get("novelID");
                if (novelIdObj instanceof Integer) {
                    novelID = (Integer) novelIdObj;
                } else if (novelIdObj instanceof Long) {
                    novelID = ((Long) novelIdObj).intValue();
                } else if (novelIdObj instanceof String) {
                    novelID = Integer.parseInt((String) novelIdObj);
                } else {
                    return ResponseEntity.badRequest().body(Map.of("success", false, "message", "novelID参数格式错误"));
                }

                // 处理groupID
                Object groupIdObj = request.get("groupID");
                if (groupIdObj != null) {
                    if (groupIdObj instanceof Integer) {
                        groupID = (Integer) groupIdObj;
                    } else if (groupIdObj instanceof String) {
                        groupID = Integer.parseInt((String) groupIdObj);
                    }
                }
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "参数格式错误"));
            }

            // 检查书籍是否已在书架
            if (bookshelfService.existsInBookshelf(userID, novelID)) {
                return ResponseEntity.status(409).body(Map.of("success", false, "message", "该小说已在书架中"));
            }

            // 添加至书架
            boolean success = bookshelfService.addToBookshelf(userID, novelID, groupID);
            if (success) {
                return ResponseEntity.ok(Map.of("success", true, "message", "加入书架成功"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "加入书架失败"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "服务器错误: " + e.getMessage()));
        }
    }

    /**
     * 从书架中移除书籍
     * @param authHeader 认证头信息
     * @param shelfID 书架项ID
     * @return 操作结果
     */
    @DeleteMapping("/{shelfID}")
    public ResponseEntity<?> removeFromBookshelf(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer shelfID) {

        Long userID = getUserIdFromToken(authHeader);
        if (userID == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "未登录"));
        }

        try {
            boolean success = bookshelfService.removeFromBookshelf(userID, shelfID);
            if (success) {
                return ResponseEntity.ok(Map.of("success", true, "message", "移除成功"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "移除失败，书籍不存在或无权操作"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "服务器错误"));
        }
    }

    /**
     * 将书籍移动到指定分组
     * @param authHeader 认证头信息
     * @param request 包含bookshelfId和newGroupId的请求体
     * @return 操作结果
     */
    @PutMapping("/move")
    public ResponseEntity<?> moveToGroup(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> request) {

        Long userID = getUserIdFromToken(authHeader);
        if (userID == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "未登录"));
        }

        try {
            Integer shelfID = null;
            Integer groupID = null;

            // 解析参数
            Object shelfIdObj = request.get("shelfID");
            Object groupIdObj = request.get("groupID");

            if (shelfIdObj instanceof Integer) {
                shelfID = (Integer) shelfIdObj;
            } else if (shelfIdObj instanceof String) {
                shelfID = Integer.parseInt((String) shelfIdObj);
            }

            if (groupIdObj instanceof Integer) {
                groupID = (Integer) groupIdObj;
            } else if (groupIdObj instanceof String) {
                groupID = Integer.parseInt((String) groupIdObj);
            }

            if (shelfID == null || groupID == null) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "参数缺失"));
            }

            boolean success = bookshelfService.moveToGroup(userID, shelfID, groupID);
            if (success) {
                return ResponseEntity.ok(Map.of("success", true, "message", "移动成功"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "移动失败"));
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "参数格式错误"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "服务器错误"));
        }
    }

    /**
     * 切换书籍的置顶状态
     * @param authHeader 认证头信息
     * @param shelfID 书架项ID
     * @param request 包含isPinned状态的请求体
     * @return 操作结果
     */
    @PutMapping("/{shelfID}/pin")
    public ResponseEntity<?> togglePin(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer shelfID,
            @RequestBody Map<String, Boolean> request) {

        Long userID = getUserIdFromToken(authHeader);
        if (userID == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "未登录"));
        }

        try {
            Boolean isPinned = request.get("isPinned");
            if (isPinned == null) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "参数缺失"));
            }

            boolean success = bookshelfService.togglePinStatus(userID, shelfID, isPinned);
            if (success) {
                return ResponseEntity.ok(Map.of("success", true, "message", "操作成功"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "操作失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "服务器错误"));
        }
    }

    /**
     * 获取当前用户的书架数据
     * @param authHeader 认证头信息
     * @return 书架数据列表
     */
    @GetMapping("/my")
    public ResponseEntity<?> getMyBookshelf(@RequestHeader("Authorization") String authHeader) {
        Long userID = getUserIdFromToken(authHeader);
        if (userID == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "未登录"));
        }

        try {
            List<Bookshelf> bookshelf = bookshelfService.getUserBookshelf(userID);
            return ResponseEntity.ok(Map.of("success", true, "data", bookshelf));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "获取书架数据失败"));
        }
    }

    /**
     * 获取当前用户书架（包含小说完整信息）
     */
    @GetMapping("/detailList")
    public ResponseEntity<?> getMyBookshelfDetail(@RequestHeader("Authorization") String authHeader) {
        Long userID = getUserIdFromToken(authHeader);
        if (userID == null) {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "未登录"
            ));
        }

        try {
            List<BookshelfNovel> bookshelf = bookshelfService.getBookshelfListWithNovel(userID);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", bookshelf
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "获取书架数据失败"
            ));
        }
    }

    /**
     * 获取当前用户的所有分组
     * @param authHeader 认证头信息
     * @return 分组列表
     */
    @GetMapping("/groups")
    public ResponseEntity<?> getMyGroups(@RequestHeader("Authorization") String authHeader) {
        Long userID = getUserIdFromToken(authHeader);
        if (userID == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "未登录"));
        }

        try {
            List<BookshelfGroup> groups = bookshelfService.getUserGroups(userID);
            return ResponseEntity.ok(Map.of("success", true, "data", groups));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "获取分组数据失败"));
        }
    }

    /**
     * 创建新的分组
     * @param authHeader 认证头信息
     * @param request 包含groupName的请求体
     * @return 操作结果
     */
    @PostMapping("/groups")
    public ResponseEntity<?> createGroup(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> request) {

        Long userID = getUserIdFromToken(authHeader);
        if (userID == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "未登录"));
        }

        try {
            String groupName = request.get("groupName");
            if (groupName == null || groupName.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "分组名称不能为空"));
            }

            boolean success = bookshelfService.createGroup(userID, groupName.trim());
            if (success) {
                return ResponseEntity.ok(Map.of("success", true, "message", "创建分组成功"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "创建分组失败"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "服务器错误"));
        }
    }

    /**
     * 更新分组名称
     * @param authHeader 认证头信息
     * @param groupID 分组ID
     * @param request 包含新groupName的请求体
     * @return 操作结果
     */
    @PutMapping("/groups/{groupID}")
    public ResponseEntity<?> updateGroup(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer groupID,
            @RequestBody Map<String, String> request) {

        Long userID = getUserIdFromToken(authHeader);
        if (userID == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "未登录"));
        }

        try {
            String groupName = request.get("groupName");
            if (groupName == null || groupName.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "分组名称不能为空"));
            }

            boolean success = bookshelfService.updateGroupName(userID, groupID, groupName.trim());
            if (success) {
                return ResponseEntity.ok(Map.of("success", true, "message", "更新分组成功"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "更新分组失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "服务器错误"));
        }
    }

    /**
     * 删除分组（仅当分组为空时）
     * @param authHeader 认证头信息
     * @param groupID 分组ID
     * @return 操作结果
     */
    @DeleteMapping("/groups/{groupID}")
    public ResponseEntity<?> deleteGroup(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer groupID) {

        Long userID = getUserIdFromToken(authHeader);
        if (userID == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "未登录"));
        }

        try {
            boolean success = bookshelfService.deleteGroup(userID, groupID);
            if (success) {
                return ResponseEntity.ok(Map.of("success", true, "message", "删除分组成功"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "分组不为空，无法删除"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "服务器错误"));
        }
    }

    /**
     * 获取书架列表
     * @param authHeader
     * @param
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<?> getBookshelfList(@RequestHeader("Authorization") String authHeader) {
        Long userID = getUserIdFromToken(authHeader);
        if (userID == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "未登录"));
        }

        try {
            List<Bookshelf> bookshelfList = bookshelfService.getUserBookshelf(userID);
            return ResponseEntity.ok(Map.of("success", true, "data", bookshelfList));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "获取书架列表失败"));
        }
    }
    
}