package me.hsgamer.demodatarest.dto.response;

import lombok.Data;

@Data
public class BookResponse {
    int id;
    String title;
    String content;
    LibraryResponse library;
}
