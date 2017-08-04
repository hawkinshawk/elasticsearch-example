package com.mkyong.book.repository;

import com.mkyong.book.model.Author;
import com.mkyong.book.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BookRepository extends ElasticsearchRepository<Book, String> {

    Page<Book> findByAuthor(Author author, Pageable pageable);

    List<Book> findByTitle(String title);

}