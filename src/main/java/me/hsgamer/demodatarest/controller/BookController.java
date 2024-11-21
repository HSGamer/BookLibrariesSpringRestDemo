package me.hsgamer.demodatarest.controller;

import me.hsgamer.demodatarest.dto.request.BookRequest;
import me.hsgamer.demodatarest.dto.response.BookResponse;
import me.hsgamer.demodatarest.model.Book;
import me.hsgamer.demodatarest.repository.BookRepository;
import me.hsgamer.demodatarest.repository.LibraryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController extends AutoMapCrudController<Book, Integer, BookRepository, BookRequest, BookResponse> implements SearchCrudController<Book, Integer, BookRepository, BookRequest, BookResponse> {
    private final BookRepository repository;
    private final LibraryRepository libraryRepository;

    public BookController(BookRepository repository, ModelMapper modelMapper, LibraryRepository libraryRepository) {
        super(modelMapper);
        this.repository = repository;
        this.libraryRepository = libraryRepository;
    }

    @Override
    protected Class<BookResponse> getResponseClass() {
        return BookResponse.class;
    }

    @Override
    protected Class<BookRequest> getRequestClass() {
        return BookRequest.class;
    }

    @Override
    protected void mapToEntity(Book book, BookRequest bookRequest) {
        book.setLibrary(libraryRepository.findById(bookRequest.getLibraryId()).orElse(null));
    }

    @Override
    protected void mapToRequest(Book book, BookRequest bookRequest) {
        bookRequest.setLibraryId(book.getLibrary().getId());
    }

    @Override
    public BookRepository getRepository() {
        return repository;
    }

    @Override
    public Book newEntity() {
        return new Book();
    }

    @Override
    public Specification<Book> getSpecification(Map<String, String> params) {
        Specification<Book> specification = null;
        if (params.containsKey("libraryId")) {
            specification = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("library").get("id"), params.get("libraryId"));
        }
        return specification;
    }
}
