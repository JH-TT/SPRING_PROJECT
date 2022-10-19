package com.mysite.sbb.Repository;

import com.mysite.sbb.Model.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
}
