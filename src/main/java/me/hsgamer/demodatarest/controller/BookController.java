package me.hsgamer.demodatarest.controller;

import me.hsgamer.demodatarest.dto.request.BookRequest;
import me.hsgamer.demodatarest.dto.response.BookResponse;
import me.hsgamer.demodatarest.model.Book;
import me.hsgamer.demodatarest.repository.BookRepository;
import me.hsgamer.demodatarest.repository.LibraryRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController extends CrudController<Book, Integer, BookRepository, BookRequest, BookResponse> {
    private final LibraryRepository libraryRepository;

    public BookController(BookRepository repository, LibraryRepository libraryRepository) {
        super(repository);
        this.libraryRepository = libraryRepository;
    }

    @Override
    protected BookResponse toResponse(Book book) {
        return BookResponse.fromEntity(book);
    }

    @Override
    protected Book newEntity() {
        return new Book();
    }

    @Override
    protected void updateEntity(Book book, BookRequest bookRequest) {
        book.setTitle(bookRequest.title());
        book.setContent(bookRequest.content());
        book.setLibrary(libraryRepository.findById(bookRequest.libraryId()).orElse(null));
    }
}
