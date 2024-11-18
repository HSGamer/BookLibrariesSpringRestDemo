package me.hsgamer.demodatarest.dto.response;

import me.hsgamer.demodatarest.model.Book;

public record BookResponse(int id, String title, String content, LibraryResponse library) {
    public static BookResponse fromEntity(Book book) {
        return new BookResponse(book.getId(), book.getTitle(), book.getContent(), LibraryResponse.fromEntity(book.getLibrary()));
    }
}
