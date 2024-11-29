package com.mysite.sbb.answer;

import com.mysite.sbb.question.Question; // Question 클래스를 임포트
import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Question question; // Question과 연관 관계 설정

    @ManyToOne
    private SiteUser author;

    private String content;

    private LocalDateTime createDate;

    // Getter 및 Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public SiteUser getAuthor() {
        return author;
    }

    public void setAuthor(SiteUser author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
}
