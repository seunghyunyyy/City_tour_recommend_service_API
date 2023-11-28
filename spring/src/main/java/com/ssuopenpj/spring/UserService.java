package com.ssuopenpj.spring;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserDTO create(String userId, String pw, String name, String phone, String email,
                          String travelDestination, String style, Boolean privacy) {
        UserDTO user = new UserDTO();

        user.setUserId(userId);
        user.setPw(EncryptionUtils.SHA256(userId, pw));
        user.setName(name);
        user.setPhone(phone);
        user.setEmail(email);
        user.setTravelDestination(travelDestination);
        user.setStyle(style);
        user.setPrivacy(privacy);
        user.setRole(UserRole.USER);

        try {
            userRepository.save(user.toEntity());
        } catch (DataIntegrityViolationException e) {
            System.out.println ("계정 생성 오류 : 이미 있는 계정");
            return null;
        }

        return user;
    }

    public UserDTO toDTO(UserEntity userEntity) {
        UserDTO user = null;
        user.setUserId(userEntity.getUserId());
        user.setPw(userEntity.getPw());
        user.setName(userEntity.getName());
        user.setPhone(userEntity.getPhone());
        user.setEmail(userEntity.getEmail());
        user.setTravelDestination(userEntity.getTravelDestination());
        user.setStyle(userEntity.getStyle());
        user.setPrivacy(userEntity.getPrivacy());
        user.setRole(userEntity.getRole());
        return user;
    }
}
