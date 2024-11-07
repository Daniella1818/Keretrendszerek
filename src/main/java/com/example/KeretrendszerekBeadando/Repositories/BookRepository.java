package com.example.KeretrendszerekBeadando.Repositories;

import com.example.KeretrendszerekBeadando.Models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
}
