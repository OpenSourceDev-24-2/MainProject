package com.mysite.sbb.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
public class UserLikesController {

    @Autowired
    private UserLikesRepository userLikesRepository;

    // 특정 유저가 누른 좋아요 조회
    /*@GetMapping("/user/{userId}")
    public List<UserLikes> getUserLikes(@PathVariable Long userId) {
        return userLikesRepository.findBySiteUser_Id(userId); // 수정된 메서드 호출
    }*/
    @GetMapping
    public List<UserLikes> getAllLikes() {
        return userLikesRepository.findAll(); // 전체 좋아요 데이터 반환
    }

    // 좋아요 추가
    @PostMapping
    public UserLikes addLike(@RequestBody UserLikes userLikes) {
        return userLikesRepository.save(userLikes);
    }

    // 좋아요 삭제
    @DeleteMapping("/{likeId}")
    public void deleteLike(@PathVariable Long likeId) {
        userLikesRepository.deleteById(likeId);
    }
}
