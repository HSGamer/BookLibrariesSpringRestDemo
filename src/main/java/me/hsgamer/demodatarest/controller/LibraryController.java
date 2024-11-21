package me.hsgamer.demodatarest.controller;

import me.hsgamer.demodatarest.dto.request.LibraryRequest;
import me.hsgamer.demodatarest.dto.response.LibraryResponse;
import me.hsgamer.demodatarest.model.Library;
import me.hsgamer.demodatarest.repository.LibraryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/libraries")
public class LibraryController extends AutoMapCrudController<Library, Integer, LibraryRepository, LibraryRequest, LibraryResponse> {
    private final LibraryRepository repository;

    public LibraryController(LibraryRepository repository, ModelMapper modelMapper) {
        super(modelMapper);
        this.repository = repository;
    }

    @Override
    protected Class<LibraryResponse> getResponseClass() {
        return LibraryResponse.class;
    }

    @Override
    protected Class<LibraryRequest> getRequestClass() {
        return LibraryRequest.class;
    }

    @Override
    protected void mapToEntity(Library library, LibraryRequest libraryRequest) {

    }

    @Override
    protected void mapToRequest(Library library, LibraryRequest libraryRequest) {

    }

    @Override
    public LibraryRepository getRepository() {
        return repository;
    }

    @Override
    public Library newEntity() {
        return new Library();
    }
}
