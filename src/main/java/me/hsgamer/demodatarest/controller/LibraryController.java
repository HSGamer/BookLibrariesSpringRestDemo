package me.hsgamer.demodatarest.controller;

import me.hsgamer.demodatarest.dto.request.LibraryRequest;
import me.hsgamer.demodatarest.dto.response.LibraryResponse;
import me.hsgamer.demodatarest.model.Library;
import me.hsgamer.demodatarest.repository.LibraryRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/libraries")
public class LibraryController extends CrudController<Library, Integer, LibraryRepository, LibraryRequest, LibraryResponse> {
    public LibraryController(LibraryRepository repository) {
        super(repository);
    }

    @Override
    protected LibraryResponse toResponse(Library library) {
        return LibraryResponse.fromEntity(library);
    }

    @Override
    protected Library newEntity() {
        return new Library();
    }

    @Override
    protected void updateEntity(Library library, LibraryRequest libraryRequest) {
        library.setName(libraryRequest.name());
    }
}
