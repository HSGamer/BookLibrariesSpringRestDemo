package me.hsgamer.demodatarest.repository;

import me.hsgamer.demodatarest.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
}