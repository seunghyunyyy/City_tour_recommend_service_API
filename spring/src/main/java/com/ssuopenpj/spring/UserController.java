package com.ssuopenpj.spring;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user/v1")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/signUp")
    public UserDTO signUp(@RequestBody SignUp user) {
        if (userRepository.getByUserId(user.getUserId()) != null) {
            System.out.println("아이디 중복");
            return null;
        }
        if (!Objects.equals(user.getPw1(), user.getPw2())) {
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
        if(!user.getPrivacy()) {
            System.out.println("개인 정보 수집 미동의");
            return null;
        }

        UserDTO userDTO = userService.create(user.getUserId(), user.getPw1(), user.getName(), user.getPhone(), user.getEmail(), user.getTravelDestination(), user.getStyle(), user.getPrivacy());

        System.out.println("----------- success create user -----------");
        System.out.println("ID : " + userDTO.getUserId());
        System.out.println("PW : " + userDTO.getPw());
        System.out.println("Name : " + userDTO.getName());
        System.out.println("Phone : " + userDTO.getPhone());
        System.out.println("Email : " + userDTO.getEmail());
        System.out.println("Travel Destination : " + userDTO.getTravelDestination());
        System.out.println("Style : " + userDTO.getStyle());
        System.out.println("Privacy : " + userDTO.getPrivacy());
        System.out.println("Role : " + userDTO.getRole());
        System.out.println("-------------------------------------------");

        return userDTO;
    }
    @PostMapping("/signIn")
    public Boolean signIn(@RequestBody SignIn user) {
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
    @GetMapping("/find/id")
    public String findId(@RequestParam String name,
                         @RequestParam String phone,
                         @RequestParam String email) {
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
}
