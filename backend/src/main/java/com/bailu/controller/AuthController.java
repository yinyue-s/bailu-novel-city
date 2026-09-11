package com.bailu.controller;

import com.bailu.entity.User;
import com.bailu.entity.dto.RegisterRequest;
import com.bailu.entity.dto.LoginRequest;
import com.bailu.entity.dto.AuthResponse;
import com.bailu.entity.dto.RefreshRequest; // 需要添加这个导入
import com.bailu.service.UserService;
import com.bailu.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*") // 添加跨域支持
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = userService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);
        if (tokenProvider.validateToken(token)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/send-code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        // 这里实现发送验证码的逻辑
        return ResponseEntity.ok(new AuthResponse(true, "验证码已发送", null, null, null));
    }

    // 修正的令牌刷新接口
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(false, "刷新令牌不能为空", null, null, null));
        }

        // 验证刷新令牌
        if (tokenProvider.validateToken(refreshToken)) {
            try {
                // 从刷新令牌中获取用户名
                String username = tokenProvider.getUsernameFromToken(refreshToken);

                // 查找用户信息
                User user = userService.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("用户不存在"));

                // 生成新的访问令牌
                String newAccessToken = tokenProvider.generateToken(user.getUsername(), user.getRole());
// 生成新的刷新令牌
                String newRefreshToken = tokenProvider.generateRefreshToken(user.getUsername());

                return ResponseEntity.ok(new AuthResponse(true, "Token刷新成功", newAccessToken, newRefreshToken, user.getUsername(), user.getRole()));

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthResponse(false, "刷新令牌无效", null, null, null));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(false, "刷新令牌无效或已过期", null, null, null));
        }
    }

    // 添加获取用户信息的接口（可选）
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);
        if (tokenProvider.validateToken(token)) {
            try {
                String username = tokenProvider.getUsernameFromToken(token);
                User user = userService.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("用户不存在"));

                // 返回用户信息（不包含密码）
                return ResponseEntity.ok(Map.of(
                        "username", user.getUsername(),
                        "email", user.getEmail(),
                        "role", user.getRole()
                ));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}