package com.mysite.sbb.question;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class QuestionDto {
    private Integer id;
    private String subject;
    private String content;
    private LocalDateTime createDate;
    private String author; // 작성자 이름
    private int recommendCount; // 추천수
}
