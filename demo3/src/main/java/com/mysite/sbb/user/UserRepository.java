package com.mysite.sbb.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
	// username으로 사용자 조회
	Optional<SiteUser> findByUsername(String username);
}
