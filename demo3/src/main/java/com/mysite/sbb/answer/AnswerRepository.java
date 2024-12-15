package com.mysite.sbb.answer;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; // List를 임포트

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestionId(Long questionId);

    List<Answer> id(Long id);
}
