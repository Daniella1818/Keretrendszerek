package com.example.KeretrendszerekBeadando.Controllers;

import com.example.KeretrendszerekBeadando.Models.Book;
import com.example.KeretrendszerekBeadando.Models.BookType;
import com.example.KeretrendszerekBeadando.Models.Borrowing;
import com.example.KeretrendszerekBeadando.Repositories.BookRepository;
import com.example.KeretrendszerekBeadando.Repositories.BorrowingRepository;
import com.example.KeretrendszerekBeadando.Services.BorrowingService;
import com.example.KeretrendszerekBeadando.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class BorrowingController {
    @Autowired
    BorrowingRepository borrowingRepository;
    @Autowired
    UserService userService;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    BorrowingService borrowingService;

    @PostMapping("/reader/borrowBook/{bookId}")
    public String borrowBook(@PathVariable("bookId") Long bookId, @RequestParam int borrowDuration) {
        Date borrowingDate = new Date();
        Book book = bookRepository.getById(bookId);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(borrowingDate);
        calendar.add(Calendar.DAY_OF_MONTH, borrowDuration);
        int price = book.getPrice() * borrowDuration;

        borrowingRepository.save(new Borrowing(userService.getLoggedInUser(), book, borrowingDate, calendar.getTime(), borrowDuration, price));
        return "redirect:/reader/showAllBooks";
    }
    @GetMapping("/reader/showBorrowedBooks")
    public String showAllBorrowingPage(Model model){
        model.addAttribute("borrowings", borrowingService.getBorrowingBooksOfLoggedIn(userService.getLoggedInUserId()));
        return "listBorrowedBooks";
    }

    //UPDATE OWN BOOK
    @GetMapping("/reader/editBorrowing/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Borrowing borrowing = borrowingRepository.getById(id);
        model.addAttribute("borrowing", borrowing);
        return "updateBorrowing";
    }

    @PostMapping("/reader/updateBorrowing/{id}")
    public String updateBorrowing(@PathVariable Long id, Borrowing b) {
        Borrowing borrowing = borrowingRepository.getById(id);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(borrowing.getBorrowingDate());
        calendar.add(Calendar.DAY_OF_MONTH, b.getBorrowingDays());

        int price = borrowing.getBook().getPrice() * b.getBorrowingDays();

        borrowing.setBorrowingDays(b.getBorrowingDays());
        borrowing.setBorrowingLastDate(calendar.getTime());
        borrowing.setBorrowingPrice(price);

        borrowingRepository.save(borrowing);
        return "redirect:/reader/showBorrowedBooks";
    }

    //DELETE
    @GetMapping("/reader/deleteBorrowing/{id}")
    public String deleteBorrowing(@PathVariable Long id) {
        Borrowing borrowing = borrowingRepository.getById(id);
        if(borrowing == null)
            return "/error";
        borrowingRepository.delete(borrowing);
        return "redirect:/reader/showBorrowedBooks";
    }
}
