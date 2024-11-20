package com.example.pageable.service;

import com.example.pageable.model.Book;
import com.example.pageable.repository.BookRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class BookService {

    private BookRepository br;

    public BookService(BookRepository br) {
        this.br = br;
    }

    public Page<Book> getAllBooks(Pageable pageable) {
        return br.findAll(pageable);
    }

    public Optional<Book> getBookById(Long id) {
        return br.findById(id);
    }

    public Book addBook(Book book) {
        return br.save(book);
    }

    public Optional<Book> updateBook(Long id, Book bookDetails) {
        return br.findById(id).map(book -> {
            book.setTitle(bookDetails.getTitle());
            book.setIsbn(bookDetails.getIsbn());
            book.setAuthor(bookDetails.getAuthor());
            book.setYear(bookDetails.getYear());
            return br.save(book);
        });
    }

    public boolean deleteBook(Long id) {
        return br.findById(id).map(book -> {br.delete(book); return true;}).orElse(false);
    }
}
