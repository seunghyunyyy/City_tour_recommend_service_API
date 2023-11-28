package com.ssuopenpj.spring;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {
    private Password password;
    private String name;
    private String phone;
    private String email;
    private String travelDestination;
    private String style;
}