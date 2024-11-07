package com.example.KeretrendszerekBeadando.Repositories;

import com.example.KeretrendszerekBeadando.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameOrEmail(String username, String email);
    boolean existsUserByUsernameAndEmail(String username, String email);
}
