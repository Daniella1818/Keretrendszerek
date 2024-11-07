package com.example.KeretrendszerekBeadando.Services;

import com.example.KeretrendszerekBeadando.Models.Book;
import com.example.KeretrendszerekBeadando.Models.Borrowing;
import com.example.KeretrendszerekBeadando.Repositories.BookRepository;
import com.example.KeretrendszerekBeadando.Repositories.BorrowingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BorrowingService borrowingService;

    public List<Book> getBooksOfOtherUsers(Long loggedInUserId) {
        // Az összes könyv, amit más felhasználók töltöttek fel
        List<Book> allBooks = bookRepository.findAll();

        // Azok a könyvek, amiket más felhasználók töltöttek fel és még nem vannak kölcsönözve
        List<Book> booksForLoggedInUser = allBooks.stream()
                .filter(book -> book.getUser() != null && !book.getUser().getId().equals(loggedInUserId))
                .filter(book -> !isBookBorrowedByUser(loggedInUserId, book.getId()))
                .collect(Collectors.toList());

        return booksForLoggedInUser;
    }
    private boolean isBookBorrowedByUser(Long userId, Long bookId){
        List<Borrowing> allBorrowingOfUser = borrowingService.getBorrowingBooksOfLoggedIn(userId);
        return allBorrowingOfUser.stream().anyMatch(borrowing -> borrowing.getBook().getId() == bookId);
    }
    public List<Book> getBooksOfLoggedUser(Long loggedInUserId) {
        List<Book> allBooks = bookRepository.findAll();
        List<Book> booksForLoggedInUser = allBooks.stream()
                .filter(book -> book.getUser() != null && book.getUser().getId().equals(loggedInUserId))
                .collect(Collectors.toList());
        return booksForLoggedInUser;
    }
}
