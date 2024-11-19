package me.hsgamer.demodatarest.dto.request;

import lombok.Data;

@Data
public class BookRequest {
    String title;
    String content;
    int libraryId;
}
