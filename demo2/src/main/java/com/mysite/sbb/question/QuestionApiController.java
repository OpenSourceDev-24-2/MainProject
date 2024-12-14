package com.mysite.sbb.question;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mysite.sbb.user.UserService;
import com.mysite.sbb.user.SiteUser;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionApiController {

    private final QuestionService questionService;
    private final AnswerService answerService; // 변수 이름 변경
    private final UserService userService;

    public QuestionApiController(QuestionService questionService, AnswerService answerService, UserService userService) {
        this.questionService = questionService;
        this.answerService = answerService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        Question question = questionService.getQuestion(id.longValue());
        if (question == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(question); // Question 엔티티 직접 반환
    }

    @GetMapping
    public Page<Question> getAllQuestions(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "kw", defaultValue = "") String keyword
    ) {
        return questionService.getList(page, keyword); // Question 엔티티를 직접 반환
    }

    // 댓글 목록 가져오기 (Answer로 변경)
    @GetMapping("/{id}/answers")
    public ResponseEntity<List<Answer>> getAnswers(@PathVariable Long id) {
        List<Answer> answers = answerService.getAnswersByQuestionId(id); // Answer 엔티티 반환
        return ResponseEntity.ok(answers);
    }

    // 게시글 작성 API 추가
    @PostMapping("/create")
    public ResponseEntity<Question> createQuestion(@RequestBody QuestionForm questionForm, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        SiteUser siteUser = userService.getUser(principal.getName());
        Question question = questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);

        return ResponseEntity.ok(question); // Question 엔티티 반환
    }
}
