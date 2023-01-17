package com.mysite.sbb.Repository;

import com.mysite.sbb.Model.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    SiteUser findByusername(String username);
    Optional<SiteUser> findByemail(String email);
}
