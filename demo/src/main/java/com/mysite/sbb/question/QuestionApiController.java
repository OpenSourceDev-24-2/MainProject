package com.mysite.sbb.question;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mysite.sbb.answer.CommentService;
import com.mysite.sbb.answer.CommentDto;
import com.mysite.sbb.user.UserService;
import com.mysite.sbb.user.SiteUser;

import java.security.Principal;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionApiController {

    private final QuestionService questionService;
    private final CommentService commentService;
    private final UserService userService;

    public QuestionApiController(QuestionService questionService, CommentService commentService, UserService userService) {
        this.questionService = questionService;
        this.commentService = commentService;
        this.userService = userService;
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

    // 게시글 작성 API 추가
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<QuestionDto> createQuestion(@RequestBody QuestionForm questionForm, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 사용자 정보 가져오기
        SiteUser siteUser = userService.getUser(principal.getName());

        // 질문 생성 및 반환된 질문 객체 저장
        Question question = questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);

        // 생성된 질문을 DTO로 변환하여 반환
        QuestionDto dto = new QuestionDto();
        dto.setId(question.getId());
        dto.setSubject(question.getSubject());
        dto.setContent(question.getContent());
        dto.setCreateDate(question.getCreateDate());
        dto.setAuthor(question.getAuthor() != null ? question.getAuthor().getUsername() : "익명");
        dto.setRecommendCount(question.getRecommendCount());

        return ResponseEntity.ok(dto);
    }
}
