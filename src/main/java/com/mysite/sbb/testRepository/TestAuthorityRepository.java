package com.mysite.sbb.testRepository;

import com.mysite.sbb.Model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestAuthorityRepository extends JpaRepository<Authority, String> {
}
