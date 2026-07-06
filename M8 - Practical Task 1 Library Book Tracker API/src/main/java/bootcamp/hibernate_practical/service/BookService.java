package bootcamp.hibernate_practical.service;

import bootcamp.hibernate_practical.dto.BookResponse;
import bootcamp.hibernate_practical.dto.CreateBookRequest;
import bootcamp.hibernate_practical.dto.UpdateBookRequest;
import bootcamp.hibernate_practical.entity.Book;
import bootcamp.hibernate_practical.exception.BookNotFoundException;
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
        List<Book> books = bookRepository.findAll();
        return new ArrayList<>(books.stream().map(this::mapToResponse).toList());
    }

    public BookResponse getBookById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            return mapToResponse(book.get());
        } else {
            throw new BookNotFoundException("Book not found with id: " + id);
        }
    }

    public BookResponse updateBook(Long id, UpdateBookRequest request) {
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
            throw new BookNotFoundException("Book not found with id: " + id);
        }
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    public List<BookResponse> findByAuthor(String author) {
        return bookRepository.findByAuthor(author).stream().map(this::mapToResponse).toList();
    }

    public List<BookResponse> findAvailableBooks(){
        return bookRepository.findByAvailableTrue().stream().map(this::mapToResponse).toList();
    }

    public List<BookResponse> findByPublicationYearGreaterThan(int publicationYear) {
        return bookRepository.findByPublicationYearGreaterThan(publicationYear).stream().map(this::mapToResponse).toList();
    }

    public long countBooks() {
        return bookRepository.count();
    }

    private BookResponse mapToResponse(Book book) {
        return new BookResponse(book.getId(), book.getTitle(), book.getAuthor(), book.getGenre(), book.getPublicationYear(), book.isAvailable());
    }
}
