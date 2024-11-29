package com.mysite.sbb.question;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mysite.sbb.answer.CommentService;
import com.mysite.sbb.answer.CommentDto;
import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionApiController {

    private final QuestionService questionService;
    private final CommentService commentService;

    public QuestionApiController(QuestionService questionService, CommentService commentService) {
        this.questionService = questionService;
        this.commentService = commentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDto> getQuestionById(@PathVariable Long id) {
        Question question = questionService.getQuestion(id.intValue());
        if (question == null) {
            return ResponseEntity.notFound().build();
        }
        QuestionDto dto = new QuestionDto();
        dto.setId(question.getId());
        dto.setSubject(question.getSubject());
        dto.setContent(question.getContent());
        dto.setCreateDate(question.getCreateDate());
        dto.setAuthor(question.getAuthor() != null ? question.getAuthor().getUsername() : "익명");
        dto.setRecommendCount(question.getRecommendCount());
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public Page<QuestionDto> getAllQuestions(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "kw", defaultValue = "") String keyword
    ) {
        return questionService.getList(page, keyword).map(question -> {
            QuestionDto dto = new QuestionDto();
            dto.setId(question.getId());
            dto.setSubject(question.getSubject());
            dto.setContent(question.getContent());
            dto.setCreateDate(question.getCreateDate());
            dto.setAuthor(question.getAuthor() != null ? question.getAuthor().getUsername() : "익명");
            dto.setRecommendCount(question.getRecommendCount());
            return dto;
        });
    }

    // 댓글 목록 가져오기
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Integer id) {
        List<CommentDto> comments = commentService.getCommentsByQuestionId(id);
        return ResponseEntity.ok(comments);
    }
}
