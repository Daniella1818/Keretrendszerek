package com.example.KeretrendszerekBeadando;

import com.example.KeretrendszerekBeadando.Models.Role;
import com.example.KeretrendszerekBeadando.Repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EntityScan("com.example.KeretrendszerekBeadando.Models")
public class KeretrendszerekBeadandoApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeretrendszerekBeadandoApplication.class, args);
	}
}
