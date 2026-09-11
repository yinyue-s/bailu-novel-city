package com.bailu.service;

import com.bailu.entity.User;
import com.bailu.entity.Role;
import com.bailu.entity.dto.RegisterRequest;
import com.bailu.entity.dto.LoginRequest;
import com.bailu.entity.dto.AuthResponse;
import com.bailu.mapper.UserMapper;
import com.bailu.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public AuthResponse register(RegisterRequest request) {
        // 检查用户名是否已存在
        if (userMapper.existsByUsername(request.getUsername())) {
            return new AuthResponse(false, "用户名已存在", null, null, null);
        }

        // 检查邮箱是否已存在
        if (request.getEmail() != null && !request.getEmail().isEmpty() &&
                userMapper.existsByEmail(request.getEmail())) {
            return new AuthResponse(false, "邮箱已被注册", null, null, null);
        }

        // 创建新用户 - 密码直接存储
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // 直接存储明文密码
        user.setRole(request.getRole() != null ? request.getRole() : Role.READER);

        int result = userMapper.insert(user);

        if (result > 0) {
            // 生成token
            String token = tokenProvider.generateToken(user.getUsername(), user.getRole());
            return new AuthResponse(true, "注册成功", token, user.getUsername(), user.getRole());
        } else {
            return new AuthResponse(false, "注册失败", null, null, null);
        }
    }

    public AuthResponse login(LoginRequest request) {
        // 先尝试按用户名查找
        Optional<User> userOpt = userMapper.findByUsername(request.getUsername());

        // 如果按用户名找不到，尝试按邮箱查找
        if (userOpt.isEmpty()) {
            userOpt = userMapper.findByEmail(request.getUsername());
        }

        // 如果还是找不到，返回错误
        if (userOpt.isEmpty()) {
            return new AuthResponse(false, "用户名或密码错误", null, null, null);
        }

        User user = userOpt.get();

        // 直接比较明文密码
        if (!user.getPassword().equals(request.getPassword())) {
            return new AuthResponse(false, "用户名或密码错误", null, null, null);
        }

        // 生成token
        String token = tokenProvider.generateToken(user.getUsername(), user.getRole());

        return new AuthResponse(true, "登录成功", token, user.getUsername(), user.getRole());
    }

    public Long findUserIDByUsername(String username) {
        Optional<User> user = userMapper.findByUsername(username);
        return user.map(User::getId).orElse(null);
    }

    public Optional<User> findByUsername(String username) {
        return userMapper.findByUsername(username);
    }
}