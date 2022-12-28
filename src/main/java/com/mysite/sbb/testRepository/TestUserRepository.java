package com.mysite.sbb.testRepository;

import com.mysite.sbb.testEntity.TestUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestUserRepository extends JpaRepository<TestUser, Long> {
    // username을 기준으로 TestUser 정보를 가져올때 권한 정보도 같이 가져오게 된다.
    // EntityGraph는 쿼리가 수행이 될때 Lazy가 아닌 Eager 조회로 authorities 정보를 같이 가져온다.
    @EntityGraph(attributePaths = "authorities")
    Optional<TestUser> findOneWithAuthoritiesByUsername(String username);
}
