package me.hsgamer.demodatarest.controller;

import me.hsgamer.demodatarest.dto.request.BookRequest;
import me.hsgamer.demodatarest.dto.response.BookResponse;
import me.hsgamer.demodatarest.model.Book;
import me.hsgamer.demodatarest.repository.BookRepository;
import me.hsgamer.demodatarest.repository.LibraryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController extends AutoMapCrudController<Book, Integer, BookRepository, BookRequest, BookResponse> {
    private final LibraryRepository libraryRepository;

    public BookController(BookRepository repository, ModelMapper modelMapper, LibraryRepository libraryRepository) {
        super(repository, modelMapper);
        this.libraryRepository = libraryRepository;
    }

    @Override
    protected Class<BookResponse> getResponseClass() {
        return BookResponse.class;
    }

    @Override
    protected void afterMap(Book book, BookRequest bookRequest) {
        book.setLibrary(libraryRepository.findById(bookRequest.getLibraryId()).orElse(null));
    }

    @Override
    protected Book newEntity() {
        return new Book();
    }
}
