package com.ssuopenpj.spring;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public String findId(String name, String phone, String email) {
        try {
            UserEntity userEntity = userRepository.findByNameAndPhoneAndEmail(name, phone, email);
            System.out.println(userEntity.getUserId());
            return userEntity.getUserId();
        } catch (NullPointerException e) {
            String error = "입력하신 이름, 전화번호, 이메일로 가입한 계정이 없습니다.";
            System.out.println(error);
            return error;
        }
    }

    public UserDTO signUp(SignUp user) {
        if (userRepository.getByUserId(user.getUserId()) != null) {
            System.out.println("아이디 중복");
            return null;
        }
        if (!Objects.equals(user.getPassword().getPw1(), user.getPassword().getPw2())) {
            System.out.println("패스워드 불일치");
            return null;
        }
        if (userRepository.getByPhone(user.getPhone()) != null) {
            System.out.println("전화번호 중복");
            return null;
        }
        if (userRepository.getByEmail(user.getEmail()) != null) {
            System.out.println("이메일 중복");
            return null;
        }
        if (!user.getPrivacy()) {
            System.out.println("개인 정보 수집 미동의");
            return null;
        }

        UserDTO userDTO = create(user.getUserId(), user.getPassword().getPw1(), user.getName(), user.getPhone(), user.getEmail(),
                user.getTravelDestination(), user.getStyle(), user.getPrivacy());

        printDTO(userDTO);

        return userDTO;
    }

    public Boolean signIn(SignIn user) {
        UserEntity userEntity;
        String userId = user.getUserId();
        String userPw = user.getPw();
        try {
            userRepository.getByUserId(userId);
        } catch (EntityNotFoundException e) {
            System.out.println("아이디 없음");
            return false;
        }

        try {
            userEntity = userRepository.findByUserId(userId);

            if (!Objects.equals(userEntity.getPw(), EncryptionUtils.SHA256(userId, userPw))) {
                System.out.println("비밀번호 불일치");
                return false;
            } else {
                System.out.println("로그인 성공!");
                return true;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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
            System.out.println("계정 생성 오류 : 이미 있는 계정");
            return null;
        }

        return user;
    }

    public void findPassword(String userId, String phone) {
        try {
            userRepository.getByUserIdAndPhone(userId, phone);
        } catch (NullPointerException e) {
            System.out.println("없는 계정");
        }
    }

    public UserDTO toDTO(UserEntity userEntity) {
        UserDTO user = new UserDTO(userEntity.getUserId(), userEntity.getPw(), userEntity.getName(), userEntity.getPhone(), userEntity.getEmail(),
                userEntity.getTravelDestination(), userEntity.getStyle(), userEntity.getPrivacy(), userEntity.getRole());
        return user;
    }

    public void printDTO(UserDTO userDTO) {
        System.out.println("------------------- UserDTO -------------------");
        System.out.println("ID : " + userDTO.getUserId());
        System.out.println("PW : " + userDTO.getPw());
        System.out.println("Name : " + userDTO.getName());
        System.out.println("Phone : " + userDTO.getPhone());
        System.out.println("Email : " + userDTO.getEmail());
        System.out.println("Travel Destination : " + userDTO.getTravelDestination());
        System.out.println("Style : " + userDTO.getStyle());
        System.out.println("Privacy : " + userDTO.getPrivacy());
        System.out.println("Role : " + userDTO.getRole());
        System.out.println("-----------------------------------------------");
    }

    public UserDTO patchPassword(String userId, String pw1, String pw2) {
        if (!Objects.equals(pw1, pw2)) {
            System.out.println("비밀번호 불일치");
            return null;
        }
        try {
            UserEntity userEntity = userRepository.findByUserId(userId);
            System.out.println("before change pw : " + userEntity.getPw());
            userEntity.updatePassword(EncryptionUtils.SHA256(userId, pw1));
            System.out.println("after change pw : " + userEntity.getPw());
            userRepository.save(userEntity);
            return toDTO(userEntity);
        } catch (NullPointerException e) {
            System.out.println("존재하지 않는 계정");
            return null;
        }
    }

    public UserDTO patchInfo(String userId, String pw1, String pw2, String newName, String newPhone, String newEmail, String newTravelDestination, String newStyle) {
        if (userRepository.getByPhone(newPhone) != null && !Objects.equals(userId, userRepository.findByPhone(newPhone).getUserId())) {
            System.out.println("전화번호 중복");
            return null;
        }
        if (userRepository.getByEmail(newEmail) != null && !Objects.equals(userId, userRepository.findByEmail(newEmail).getUserId())) {
            System.out.println("이메일 중복");
            return null;
        }
        try {
            UserEntity userEntity = userRepository.findByUserId(userId);

            if (pw1 != null) {
                if (Objects.equals(pw1, pw2)) {
                    String newPw = EncryptionUtils.SHA256(userId, pw1);
                    if (!Objects.equals(newPw, userEntity.getPw())) {
                        System.out.println("Update name : " + userEntity.getPw() + " -> " + newPw);
                        userEntity.updatePassword(newPw);
                    } else {
                        System.out.println("동일한 비밀번호");
                    }
                } else {
                    System.out.println("비밀번호 불일치");
                    return null;
                }
            }
            if (newName != null) {
                if (!Objects.equals(userEntity.getName(), newName)) {
                    System.out.println("Update name : " + userEntity.getName() + " -> " + newName);
                    userEntity.updateName(newName);
                } else {
                    System.out.println("동일한 이름");
                }
            }
            if (newPhone != null) {
                if (!Objects.equals(userEntity.getPhone(), newPhone)) {
                    System.out.println("Update phone : " + userEntity.getPhone() + " -> " + newPhone);
                    userEntity.updatePhone(newPhone);
                } else {
                    System.out.println("동일한 전화번호");
                }
            }
            if (newEmail != null) {
                if (!Objects.equals(userEntity.getEmail(), newEmail)) {
                    System.out.println("Update email : " + userEntity.getEmail() + " -> " + newEmail);
                    userEntity.updateEmail(newEmail);
                } else {
                    System.out.println("동일한 이메일");
                }
            }
            if (newTravelDestination != null) {
                if (!Objects.equals(userEntity.getTravelDestination(), newTravelDestination)) {
                    System.out.println("Update travelDestination : " + userEntity.getTravelDestination() + " -> " + newTravelDestination);
                    userEntity.updateTravelDestination(newTravelDestination);
                } else {
                    System.out.println("동일한 선호여행지");
                }
            }
            if (newStyle != null) {
                if (!Objects.equals(userEntity.getStyle(), newStyle)) {
                    System.out.println("Update style : " + userEntity.getStyle() + " -> " + newStyle);
                    userEntity.updateStyle(newStyle);
                } else {
                    System.out.println("동일한 여행스타일");
                }
            }
            userRepository.save(userEntity);
            return toDTO(userEntity);
        } catch (NullPointerException e) {
            System.out.println("없는 계정");
            return null;
        }
    }
}
