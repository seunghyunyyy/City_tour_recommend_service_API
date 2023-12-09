package com.ssuopenpj.spring.GPT;


import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
public class Message {

    private String role;
    private String content;

    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }
}