package me.hsgamer.demodatarest.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String content;
    @ManyToOne
    @JoinColumn(name = "library_id")
    private Library library;
}
