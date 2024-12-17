package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {

	private final QuestionRepository questionRepository;

	@SuppressWarnings("unused")
	private Specification<Question> search(String kw) {
		return new Specification<>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true); // 중복을 제거
				Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
				Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
				Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
				return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
						cb.like(q.get("content"), "%" + kw + "%"), // 내용
						cb.like(u1.get("username"), "%" + kw + "%"), // 질문 작성자
						cb.like(a.get("content"), "%" + kw + "%"), // 답변 내용
						cb.like(u2.get("username"), "%" + kw + "%")); // 답변 작성자
			}
		};
	}

	public Page<Question> getList(int page, String kw) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return this.questionRepository.findAllByKeyword(kw, pageable);
	}

	public Question getQuestion(Long id) {
		Optional<Question> question = this.questionRepository.findById(id);
		if (question.isPresent()) {
			return question.get();
		} else {
			throw new DataNotFoundException("질문을 찾을 수 없습니다.");
		}
	}

	public Question create(String subject, String content, SiteUser user) {
		Question q = new Question();
		q.setSubject(subject);
		q.setContent(content);
		q.setCreateDate(LocalDateTime.now());
		q.setAuthor(user);
		Question savedQuestion = this.questionRepository.save(q);
		return savedQuestion; // 생성된 Question 객체를 반환합니다.
	}


	public void modify(Question question, String subject, String content) {
		question.setSubject(subject);
		question.setContent(content);
		question.setModifyDate(LocalDateTime.now());
		this.questionRepository.save(question);
	}

	public void delete(Question question) {
		this.questionRepository.delete(question);
	}

	public void vote(Question question, SiteUser siteUser) {
		question.getVoter().add(siteUser);
		this.questionRepository.save(question);
	}

	/**
	 * 게시글 삭제 메서드
	 *
	 * @param id          삭제할 게시글 ID
	 * @param currentUser 현재 로그인한 사용자 이름
	 * @return 삭제 성공 여부
	 */
	@Transactional
	public boolean deleteQuestion(Long id, String currentUser) {
		Question question = questionRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("게시글을 찾을 수 없습니다."));

		if (question.getAuthor().getUsername().equals(currentUser)) {
			questionRepository.delete(question);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 게시글 수정 메서드
	 *
	 * @param id           수정할 게시글 ID
	 * @param questionForm 수정할 게시글 정보
	 * @param currentUser  현재 로그인한 사용자 이름
	 * @return 수정 성공 여부
	 */
	@Transactional
	public boolean updateQuestion(Long id, QuestionForm questionForm, String currentUser) {
		Question question = questionRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("게시글을 찾을 수 없습니다."));

		if (question.getAuthor().getUsername().equals(currentUser)) {
			question.setSubject(questionForm.getSubject());
			question.setContent(questionForm.getContent());
			questionRepository.save(question);
			return true;
		} else {
			return false;
		}
	}
}
