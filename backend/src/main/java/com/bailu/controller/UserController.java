package com.bailu.controller;

import com.bailu.entity.User;
import com.bailu.entity.dto.AuthResponse;
import com.bailu.entity.dto.UserProfileResponse;
import com.bailu.mapper.UserMapper;
import com.bailu.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(false, "未登录", null, null, null));
        }

        try {
            String token = authHeader.substring(7);
            if (!tokenProvider.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthResponse(false, "Token无效", null, null, null));
            }

            String username = tokenProvider.getUsernameFromToken(token);
            Optional<User> userOpt = userMapper.findByUsername(username);

            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new AuthResponse(false, "用户不存在", null, null, null));
            }

            User user = userOpt.get();

            // 返回用户信息（不包含密码）
            UserProfileResponse profileResponse = new UserProfileResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole(),
                    user.getCreatedAt()
            );

            return ResponseEntity.ok(profileResponse);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse(false, "服务器错误", null, null, null));
        }
    }
}