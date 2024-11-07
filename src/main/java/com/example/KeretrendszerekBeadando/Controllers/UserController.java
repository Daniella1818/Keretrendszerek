package com.example.KeretrendszerekBeadando.Controllers;

import com.example.KeretrendszerekBeadando.Models.Borrowing;
import com.example.KeretrendszerekBeadando.Models.Role;
import com.example.KeretrendszerekBeadando.Models.User;
import com.example.KeretrendszerekBeadando.Repositories.RoleRepository;
import com.example.KeretrendszerekBeadando.Repositories.UserRepository;
import com.example.KeretrendszerekBeadando.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

@Controller
public class UserController {
    @Autowired
    UserRepository repository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserService service;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/public/register")
    public String showSignUpForm(User user, Model model) {
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/public/createUser")
    public String addUser(@ModelAttribute("user") User user, @RequestParam String role) {
        System.out.println("valami");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role defaultRole = roleRepository.findByName("Reader");
        Role ownerRole = roleRepository.findByName("BookOwner");
        Role adminRole = roleRepository.findByName("Admin");
        user.getRoles().add(defaultRole);

        if("BookOwner".equals(role))
            user.getRoles().add(ownerRole);
        else if("Admin".equals(role)) {
            user.getRoles().add(ownerRole);
            user.getRoles().add(adminRole);
        }

        if(!service.existsUserByUsernameAndEmail(user.getUsername(), user.getEmail())){
            repository.save(user);
            return "redirect:/login";
        }
        return "redirect:/public/register?error";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout";
    }

    @GetMapping("/admin/showAllUsers")
    public String showAllUser(Model model){
        model.addAttribute("users", repository.findAll());
        return "listUsers";
    }
    @GetMapping("/admin/deleteUser/{id}")
    public String deleteBorrowing(@PathVariable Long id) {
        User user = repository.getById(id);
        if(user == null)
            return "/error";
        repository.delete(user);
        return "listUsers";
    }
}
