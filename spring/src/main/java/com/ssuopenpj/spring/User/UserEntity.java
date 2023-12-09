package com.ssuopenpj.spring.User;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String userId;
    private String pw;
    private String name;
    @Column(unique = true)
    private String phone;
    @Column(unique = true)
    private String email;
    private String travelDestination;
    private String style;
    private Boolean privacy;
    private UserRole role;

    @Builder
    public UserEntity(String userId, String pw, String name, String phone, String email, String travelDestination, String style, Boolean privacy, UserRole role) {
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

    public void updatePassword(String newPw) {
        this.pw = newPw;
    }
    public void updateName(String newName) {
        this.name = newName;
    }
    public void updatePhone(String newPhone) {
        this.phone = newPhone;
    }
    public void updateEmail(String newEmail) {
        this.email = newEmail;
    }
    public void updateTravelDestination(String newTravelDestination) {
        this.travelDestination = newTravelDestination;
    }
    public void updateStyle(String newStyle) {
        this.style = newStyle;
    }
    public void updateRole(UserRole newRole) {
        this.role = newRole;
    }
}