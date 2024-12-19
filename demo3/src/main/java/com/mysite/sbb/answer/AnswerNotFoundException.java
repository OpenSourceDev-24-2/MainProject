package com.mysite.sbb.answer;

// AnswerNotFoundException.java
public class AnswerNotFoundException extends RuntimeException {
    public AnswerNotFoundException(String message) {
        super(message);
    }
}
