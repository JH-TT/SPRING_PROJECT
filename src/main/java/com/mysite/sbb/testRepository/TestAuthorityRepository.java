package com.mysite.sbb.testRepository;

import com.mysite.sbb.testEntity.TestAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestAuthorityRepository extends JpaRepository<TestAuthority, String> {
}
