package com.mysite.sbb.answer;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByQuestionId(Integer questionId);
}
