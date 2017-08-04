package com.mkyong.book.service;

import com.mkyong.book.model.Author;
import com.mkyong.book.model.Book;

import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface BookService {

    Book save(Book book);

    void delete(Book book);

    Book findOne(String id);

    Iterable<Book> findAll();

    Page<Book> findByAuthor(Author author, PageRequest pageRequest);

    List<Book> findByTitle(String title);

    public Iterable<Book> search(QueryBuilder queryBuilder);

}