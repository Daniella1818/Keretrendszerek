package com.example.KeretrendszerekBeadando.Controllers;

import com.example.KeretrendszerekBeadando.Models.Book;
import com.example.KeretrendszerekBeadando.Models.BookType;
import com.example.KeretrendszerekBeadando.Repositories.BookRepository;
import com.example.KeretrendszerekBeadando.Services.BookService;
import com.example.KeretrendszerekBeadando.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class BookController {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserService service;
    @Autowired
    private BookService bookService;

    //GET ALL
    @GetMapping("/reader/showAllBooks")
    public String showAllBookPage(Model model){
        model.addAttribute("books", bookService.getBooksOfOtherUsers(service.getLoggedInUserId()));
        return "listBooks";
    }

    //CREATE
    @GetMapping("/book-owner/addBook")
    public String showAddForm(Book book, Model model){
        model.addAttribute("book", book);
        model.addAttribute("bookTypes", BookType.values());
        return "addBook";
    }

    @PostMapping("/book-owner/createBook")
    public String createBook(@ModelAttribute("book") Book book, @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        byte[] imageData = imageFile.getBytes();

        // Kép mentése a fájlrendszerbe
        String imagePath = "src/main/resources/static/images/" + fileName;
        Path path = Paths.get(imagePath);
        Files.write(path, imageData);

        bookRepository.save(new Book(book.getTitle(), book.getDescription(), book.getPrice(), book.getPageNumber(),
                book.getAuthor(), book.getLanguage(), book.getType(), fileName, service.getLoggedInUser()));
        return "redirect:/reader/showAllBooks";
    }

    //GET ALL OWN BOOK
    @GetMapping("/book-owner/showOwnBooks")
    public String showOwnBookPage(Model model){
        model.addAttribute("books", bookService.getBooksOfLoggedUser(service.getLoggedInUserId()));
        return "listOwnBooks";
    }

    //UPDATE OWN BOOK
    @GetMapping("/book-owner/editBook/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Book book = bookRepository.getById(id);
        model.addAttribute("book", book);
        model.addAttribute("bookTypes", BookType.values());
        return "updateBook";
    }

    @PostMapping("/book-owner/updateBook/{id}")
    public String updateBook(@PathVariable Long id, Book b) {
        Book book = bookRepository.getById(id);

        book.setTitle(b.getTitle());
        book.setDescription(b.getDescription());
        book.setPrice(b.getPrice());
        book.setPageNumber(b.getPageNumber());
        book.setLanguage(b.getLanguage());
        book.setType(b.getType());
        book.setAuthor(b.getAuthor());

        bookRepository.save(book);
        return "redirect:/book-owner/showOwnBooks";
    }

    //DELETE
    @GetMapping("/book-owner/deleteBook/{id}")
    public String deleteBook(@PathVariable Long id) {
        Book book = bookRepository.getById(id);
        if(book != null)
        {
            String fileName = book.getImage();

            bookRepository.delete(book);

            if (fileName != null && !fileName.isEmpty()) {
                String imagePath = "src/main/resources/static/images/" + fileName;
                Path path = Paths.get(imagePath);
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "redirect:/book-owner/showOwnBooks";
    }
}
