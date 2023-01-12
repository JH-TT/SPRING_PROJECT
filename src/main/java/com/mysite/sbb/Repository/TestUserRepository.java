package com.mysite.sbb.Repository;

import com.mysite.sbb.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestUserRepository extends JpaRepository<User, Long> {
    User findByusername(String username);
}
