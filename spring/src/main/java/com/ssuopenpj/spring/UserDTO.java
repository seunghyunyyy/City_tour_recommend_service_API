package com.ssuopenpj.spring;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.User;
import org.apache.catalina.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private String userId;
    private String pw;
    private String name;
    private String phone;
    private String email;
    private String travelDestination;
    private String style;
    private Boolean privacy;
    private UserRole role;

    @Builder
    public UserDTO(String userId, String pw, String name, String phone, String email, String travelDestination, String style, Boolean privacy, UserRole role) {
        this.userId = userId;
        this.pw = pw;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.travelDestination = travelDestination;
        this.style = style;
        this.privacy = privacy;
        this.role = role;
    }
    public UserEntity toEntity() {
        return UserEntity.builder()
                .userId(userId)
                .pw(pw)
                .name(name)
                .phone(phone)
                .email(email)
                .travelDestination(travelDestination)
                .style(style)
                .privacy(privacy)
                .role(UserRole.USER)
                .build();
    }
}