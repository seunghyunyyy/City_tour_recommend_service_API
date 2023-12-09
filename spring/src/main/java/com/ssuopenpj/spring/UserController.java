package com.ssuopenpj.spring;

import com.ssuopenpj.spring.User.*;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public String findPassword(@RequestParam String userId, @RequestParam String phone) {
        return userService.findPassword(userId, phone);
    }
    @PatchMapping("/{userId}/update/password")
    public UserDTO patchPassword(@PathVariable(name = "userId") String userId, @RequestBody Password password) {
        return userService.patchPassword(userId, password.getPw1(), password.getPw2());
    }
    @PatchMapping("/{userId}/update/info")
    public UserDTO patchInfo(@PathVariable(name = "userId") String userId, @RequestBody UserInfo info) {
        return userService.patchInfo(userId, info.getPassword().getPw1(), info.getPassword().getPw2(), info.getName(),
                                     info.getPhone(), info.getEmail(), info.getTravelDestination(), info.getStyle());
    }
    @DeleteMapping("/withdrawal")
    public Boolean withdrawalAccount(@RequestBody SignIn user) {
        return userService.withdrawalAccount(user);
    }
}
