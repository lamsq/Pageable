package com.example.pageable;

import com.example.pageable.model.Book;
import com.example.pageable.repository.BookRepository;
import com.example.pageable.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    private BookRepository bookRepository;
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        bookRepository = mock(BookRepository.class);

        bookService = new BookService(bookRepository);
    }

    @Test
    public void testGetAllBooks() {
        Book book1 = new Book();
        book1.setTitle("Book 1");

        Book book2 = new Book();
        book2.setTitle("Book 2");

        Page<Book> booksPage = new PageImpl<>(Arrays.asList(book1, book2));
        when(bookRepository.findAll(any(PageRequest.class))).thenReturn(booksPage);

        Page<Book> result = bookService.getAllBooks(PageRequest.of(0, 10));

        assertEquals(2, result.getContent().size());
        assertEquals("Book 1", result.getContent().get(0).getTitle());
    }

    @Test
    public void testGetBookById_Found() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<Book> result = bookService.getBookById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Book", result.get().getTitle());
    }

    @Test
    public void testGetBookById_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Book> result = bookService.getBookById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    public void testAddBook() {
        Book book = new Book();
        book.setTitle("New Book");

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.addBook(book);

        assertNotNull(result);
        assertEquals("New Book", result.getTitle());
    }

    @Test
    public void testDeleteBook() {
        Book book = new Book();
        book.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).delete(book);

        boolean result = bookService.deleteBook(1L);

        assertTrue(result);
        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    public void testDeleteBook_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = bookService.deleteBook(1L);

        assertFalse(result);
    }
}
