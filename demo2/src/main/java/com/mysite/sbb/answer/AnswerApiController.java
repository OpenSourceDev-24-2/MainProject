package com.mysite.sbb.answer;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/answers")
@RequiredArgsConstructor
public class AnswerApiController {

    private final AnswerService answerService;
    private final UserService userService;

    // DELETE /api/answers/{id}
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteAnswer(@PathVariable("id") Long id, Principal principal) {
        try {
            SiteUser currentUser = userService.getUser(principal.getName());
            answerService.deleteAnswer(id, currentUser);
            return ResponseEntity.ok(Map.of("message", "댓글이 성공적으로 삭제되었습니다."));
        } catch (AnswerNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", "댓글을 찾을 수 없습니다."));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(403).body(Map.of("message", "댓글 삭제 권한이 없습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "서버 오류가 발생했습니다."));
        }
    }
}
