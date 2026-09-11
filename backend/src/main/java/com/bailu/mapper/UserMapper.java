package com.bailu.mapper;

import com.bailu.entity.User;
import com.bailu.entity.Role;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO user (username, email, password, role) " +
            "VALUES (#{username}, #{email}, #{password}, #{role})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Select("SELECT * FROM user WHERE username = #{username}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "email", column = "email"),
            @Result(property = "password", column = "password"),
            @Result(property = "role", column = "role"),
            @Result(property = "createdAt", column = "created_at")
    })
    Optional<User> findByUsername(String username);

    // 添加按邮箱查询的方法
    @Select("SELECT * FROM user WHERE email = #{email}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "email", column = "email"),
            @Result(property = "password", column = "password"),
            @Result(property = "role", column = "role"),
            @Result(property = "createdAt", column = "created_at")
    })
    Optional<User> findByEmail(String email);

    @Select("SELECT COUNT(*) FROM user WHERE username = #{username}")
    boolean existsByUsername(String username);

    @Select("SELECT COUNT(*) FROM user WHERE email = #{email}")
    boolean existsByEmail(String email);
}