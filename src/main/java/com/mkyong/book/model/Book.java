package com.mkyong.book.model;

import org.joda.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Document(indexName = "book_index_alias", type = "book")
public class Book {

    @Id
    private String id;

    private String zusammenfassung;

    private String title;

    @Field(type = FieldType.Nested)
    private Author author;

    @Field(type = FieldType.Date)
    private LocalDate releaseDate;



    @Field(type = FieldType.Nested)
    private Issue issue;

    public Book() {
    }

    public Book(String id, String title, Author author, LocalDate releaseDate, String zusammenfassung,
            Issue issue) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.releaseDate = releaseDate;
        this.zusammenfassung = zusammenfassung;
        this.issue = issue;
    }

    public Book(String id, String title, Author author, LocalDate releaseDate, String zusammenfassung) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.releaseDate = releaseDate;
        this.zusammenfassung = zusammenfassung;
    }

    public Book(String id, String title, Author author, LocalDate releaseDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.releaseDate = releaseDate;
    }



    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", zusammenfassung='" + zusammenfassung + '\'' +
                ", issue='" + issue + '\'' +
                '}';
    }

    public String getZusammenfassung() {
        return zusammenfassung;
    }

    public void setZusammenfassung(String zusammenfassung) {
        this.zusammenfassung = zusammenfassung;
    }
}
