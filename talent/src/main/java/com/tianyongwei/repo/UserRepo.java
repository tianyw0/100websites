package com.tianyongwei.repo;

import com.tianyongwei.entity.core.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {

    List<User> findByEmailAndIsVerified(String email, Boolean isVerified);

    List<User> findByEmail(String email);
}
