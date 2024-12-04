package com.mysite.sbb.answer;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<CommentDto> getCommentsByQuestionId(Integer questionId) {
        // Comment 엔티티를 CommentDto로 변환
        return commentRepository.findByQuestionId(questionId).stream()
                .map(comment -> {
                    CommentDto dto = new CommentDto();
                    dto.setId(comment.getId());
                    dto.setContent(comment.getContent());
                    dto.setAuthor(comment.getAuthor().getUsername());
                    dto.setCreateDate(comment.getCreateDate());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
