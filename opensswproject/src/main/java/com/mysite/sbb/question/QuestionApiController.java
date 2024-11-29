package com.mysite.sbb.question;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/questions")
public class QuestionApiController {

    private final QuestionService questionService;

    public QuestionApiController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public Page<QuestionDto> getAllQuestions(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "kw", defaultValue = "") String keyword
    ) {
        // Question 데이터를 DTO로 변환하여 반환
        return questionService.getList(page, keyword).map(question -> {
            QuestionDto dto = new QuestionDto();
            dto.setId(question.getId());
            dto.setSubject(question.getSubject());
            dto.setContent(question.getContent());
            dto.setCreateDate(question.getCreateDate());
            dto.setAuthor(question.getAuthor() != null ? question.getAuthor().getUsername() : "익명");
            dto.setRecommendCount(question.getRecommendCount()); // 추천수
            return dto;
        });
    }
}
