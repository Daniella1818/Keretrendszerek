package com.example.KeretrendszerekBeadando.Models;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;

import java.util.Date;

@Entity
@Table(name = "borrowings")
public class Borrowing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Temporal(TemporalType.DATE)
    private Date borrowingDate;

    @Temporal(TemporalType.DATE)
    private Date borrowingLastDate;

    @Column(name = "borrowing_days", nullable = false)
    private int borrowingDays;

    @Column(name = "borrowing_price", nullable = false)
    private int borrowingPrice;

    public Borrowing(User user, Book book, Date borrowingDate, Date borrowingLastDate, int days, int borrowingPrice) {
        this.user = user;
        this.book = book;
        this.borrowingDate = borrowingDate;
        this.borrowingLastDate = borrowingLastDate;
        this.borrowingPrice = borrowingPrice;
        this.borrowingDays = days;
    }

    public Borrowing() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Date getBorrowingDate() {
        return borrowingDate;
    }

    public void setBorrowingDate(Date borrowingDate) {
        this.borrowingDate = borrowingDate;
    }
    public int getBorrowingDays() {
        return borrowingDays;
    }

    public void setBorrowingDays(int borrowingDays) {
        this.borrowingDays = borrowingDays;
    }

    public int getBorrowingPrice() {
        return borrowingPrice;
    }

    public void setBorrowingPrice(int borrowingPrice) {
        this.borrowingPrice = borrowingPrice;
    }
    public Date getBorrowingLastDate() {
        return borrowingLastDate;
    }

    public void setBorrowingLastDate(Date borrowingLastDate) {
        this.borrowingLastDate = borrowingLastDate;
    }
}
