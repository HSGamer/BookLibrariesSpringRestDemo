package me.hsgamer.demodatarest.dto.response;

import me.hsgamer.demodatarest.model.Library;

public record LibraryResponse(int id, String name) {
    public static LibraryResponse fromEntity(Library library) {
        return new LibraryResponse(library.getId(), library.getName());
    }
}
