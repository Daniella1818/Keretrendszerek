package com.example.KeretrendszerekBeadando.Repositories;

import com.example.KeretrendszerekBeadando.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
