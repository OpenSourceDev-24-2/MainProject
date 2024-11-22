package com.mysite.sbb.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserLikesRepository extends JpaRepository<UserLikes, Long> {
    // 특정 유저(SiteUser)가 누른 좋아요를 조회하는 메서드
    List<UserLikes> findBySiteUser_Id(Long userId);
}
