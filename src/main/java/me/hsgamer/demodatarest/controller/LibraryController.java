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
    public LibraryController(LibraryRepository repository, ModelMapper modelMapper) {
        super(repository, modelMapper);
    }

    @Override
    protected Class<LibraryResponse> getResponseClass() {
        return LibraryResponse.class;
    }

    @Override
    protected void afterMap(Library library, LibraryRequest libraryRequest) {

    }

    @Override
    protected Library newEntity() {
        return new Library();
    }
}
