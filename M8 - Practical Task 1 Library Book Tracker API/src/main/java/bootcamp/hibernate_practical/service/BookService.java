package bootcamp.hibernate_practical.service;

import bootcamp.hibernate_practical.dto.BookResponse;
import bootcamp.hibernate_practical.dto.CreateBookRequest;
import bootcamp.hibernate_practical.dto.UpdateBookRequest;
import bootcamp.hibernate_practical.entity.Book;
import bootcamp.hibernate_practical.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookResponse createBook(CreateBookRequest request) {
        Book book = new Book(
                request.getTitle(),
                request.getAuthor(),
                request.getGenre(),
                request.getPublicationYear(),
                true
        );
        Book savedBook = bookRepository.save(book);
        return mapToResponse(savedBook);
    }

    public List<BookResponse> getAllBooks() {
        // TODO:
        // Fetch all books from the repository
        // Convert each Book entity into BookResponse DTO
        // Return the list
        List<Book> books = bookRepository.findAll();
        return new ArrayList<>(books.stream().map(this::mapToResponse).toList());
    }

    public BookResponse getBookById(Long id) {
        // TODO
        // Find the book by its ID
        // Throw RuntimeException if not found
        // Convert the entity to BookResponse
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            return mapToResponse(book.get());
        } else {
            throw new RuntimeException("Book not found with id: " + id);
        }
    }

    public BookResponse updateBook(Long id, UpdateBookRequest request) {
        // TODO
        // Find existing book
        // Update its fields
        // Save the updated entity
        // Convert to BookResponse
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            Book existingBook = book.get();
            existingBook.setTitle(request.getTitle());
            existingBook.setAuthor(request.getAuthor());
            existingBook.setGenre(request.getGenre());
            existingBook.setPublicationYear(request.getPublicationYear());
            existingBook.setAvailable(request.isAvailable());
            Book updatedBook = bookRepository.save(existingBook);
            return mapToResponse(updatedBook);
        } else {
            throw new RuntimeException("Book not found with id: " + id);
        }
    }

    public void deleteBook(Long id) {
        // TODO
        bookRepository.deleteById(id);
    }

    public List<BookResponse> findByAuthor(String author) {
        // TODO
        return bookRepository.findByAuthor(author).stream().map(this::mapToResponse).toList();
    }

    public List<BookResponse> findAvailableBooks(){
        // TODO
        return bookRepository.findByAvailableTrue().stream().map(this::mapToResponse).toList();
    }

    private BookResponse mapToResponse(Book book) {
        // TODO: map Book to BookResponse
        return new BookResponse(book.getId(), book.getTitle(), book.getAuthor(), book.getGenre(), book.getPublicationYear(), book.isAvailable());
    }
}
