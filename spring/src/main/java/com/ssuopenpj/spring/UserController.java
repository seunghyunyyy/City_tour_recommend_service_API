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

    @PostMapping("/signUp")
    public UserDTO signUp(@RequestBody SignUp user) {
        return userService.signUp(user);
    }
    @PostMapping("/signIn")
    public Boolean signIn(@RequestBody SignIn user) {
        return userService.signIn(user);
    }
    @GetMapping("/find/id")
    public String findId(@RequestParam String name, @RequestParam String phone, @RequestParam String email) {
        return userService.findId(name, phone, email);
    }
    @GetMapping("/find/password")
    public void findPassword(@RequestParam String userId, @RequestParam String phone) {
        userService.findPassword(userId, phone);
    }
    @PatchMapping("/{userId}/update/password")
    public UserDTO patchPassword(@PathVariable(name = "userId") String userId, @RequestBody PasswordChange user) {
        return userService.patchPassword(userId, user.getPw1(), user.getPw2());
    }
    @PatchMapping("/{userId}/update/info")
    public UserDTO patchInfo(@PathVariable(name = "userId") String userId, @RequestBody UserInfo info) {
        return userService.patchInfo(userId, info.getPassword().getPw1(), info.getPassword().getPw2(), info.getName(),
                                     info.getPhone(), info.getEmail(), info.getTravelDestination(), info.getStyle());
    }
}
