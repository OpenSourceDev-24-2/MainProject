package com.mysite.sbb.question;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerRequest;
import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
public class QuestionApiController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    public QuestionApiController(QuestionService questionService, AnswerService answerService, UserService userService) {
        this.questionService = questionService;
        this.answerService = answerService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        Question question = questionService.getQuestion(id);
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

    // 댓글 목록 가져오기
    @GetMapping("/{id}/answers")
    public ResponseEntity<List<Answer>> getAnswers(@PathVariable Long id) {
        List<Answer> answers = answerService.getAnswersByQuestionId(id);
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

        return ResponseEntity.ok(question);
    }

    // **댓글 작성 API 추가**
    @PostMapping("/{id}/answers")
    public ResponseEntity<?> createAnswer(@PathVariable Long id,
                                          @RequestBody AnswerRequest answerRequest,
                                          Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인이 필요합니다."));
        }

        try {
            SiteUser currentUser = userService.getUser(principal.getName());
            Question question = questionService.getQuestion(id);

            Answer answer = answerService.create(question, answerRequest.getContent(), currentUser);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "댓글이 성공적으로 작성되었습니다.", "answer", answer));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "댓글 작성에 실패했습니다."));
        }
    }
}
