package com.example.KeretrendszerekBeadando.Config;
import com.example.KeretrendszerekBeadando.Models.Role;
import com.example.KeretrendszerekBeadando.Repositories.RoleRepository;
import com.example.KeretrendszerekBeadando.Services.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    RoleRepository roleRepository;

    @PostConstruct
    private void init() {
        initializeRoles();
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests(authorize ->
                        authorize
                                .requestMatchers("/", "/public/**").permitAll()
                                .requestMatchers("/reader/**").hasAuthority("Reader")
                                .requestMatchers("/book-owner/**").hasAuthority("BookOwner")
                                .requestMatchers("/admin/**").hasAuthority("Admin")
                                .anyRequest().authenticated()
                )
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/reader/showAllBooks")
                                .permitAll()
                )
                .logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                )
                .exceptionHandling()
                        .accessDeniedPage("/access-denied");
        return http.build();
    }

    private void initializeRoles() {
        List<String> roleNames = Arrays.asList("Reader", "BookOwner", "Admin");

        for (String roleName : roleNames) {
            Role existingRole = roleRepository.findByName(roleName);
            if (existingRole == null) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
        }
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}