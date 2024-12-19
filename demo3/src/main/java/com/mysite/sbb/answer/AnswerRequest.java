package com.mysite.sbb.answer;

public class AnswerRequest {
    private String content;

    // 기본 생성자
    public AnswerRequest() {}

    // 생성자
    public AnswerRequest(String content) {
        this.content = content;
    }

    // Getter 및 Setter
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
