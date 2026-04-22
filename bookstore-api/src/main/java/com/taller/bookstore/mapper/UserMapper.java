package com.taller.bookstore.mapper;

import com.taller.bookstore.dto.request.RegisterRequest;
import com.taller.bookstore.dto.response.UserResponse;
import com.taller.bookstore.entity.Role;
import com.taller.bookstore.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        // password se setea en el service (con encode)
        user.setRole(Role.USER);
        return user;
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
