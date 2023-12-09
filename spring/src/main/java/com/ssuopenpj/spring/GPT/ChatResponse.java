package com.ssuopenpj.spring.GPT;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Data
@NoArgsConstructor
public class ChatResponse {

    private List<Choice> choices;

    // constructors, getters and setters

    @Getter
    @Setter
    @Data
    @NoArgsConstructor
    public static class Choice {

        private int index;
        private Message message;

        public Choice(int index, Message message) {
            this.index = index;
            this.message = message;
        }
    }
}