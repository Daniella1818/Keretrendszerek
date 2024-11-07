package com.example.KeretrendszerekBeadando.Services;

import com.example.KeretrendszerekBeadando.Models.Book;
import com.example.KeretrendszerekBeadando.Models.Borrowing;
import com.example.KeretrendszerekBeadando.Repositories.BorrowingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowingService {
    @Autowired
    BorrowingRepository repository;

    public List<Borrowing> getBorrowingBooksOfLoggedIn(Long userId){
        List<Borrowing> allBorrowing = repository.findAll();
        List<Borrowing> borrowingsOfuser = allBorrowing.stream()
                .filter(book -> book.getUser() != null && book.getUser().getId().equals(userId))
                .collect(Collectors.toList());
        return borrowingsOfuser;
    }
}
