package com.mysite.sbb.Repository;

import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.Model.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUserDTO> findByusername(String username);
}
