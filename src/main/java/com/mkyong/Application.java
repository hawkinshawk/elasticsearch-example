package com.mkyong;

import com.mkyong.book.model.Author;
import com.mkyong.book.model.Book;
import com.mkyong.book.model.Issue;
import com.mkyong.book.service.BookService;

import org.elasticsearch.client.Client;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.util.Map;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private ElasticsearchOperations es;

    @Autowired
    private BookService bookService;

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Iterable<Book> result;
        printElasticSearchInfo();

        Author author1 = new Author("Rambabu Posa", new LocalDate(1986, 5, 23));
        LocalDate author2Birthdate = new LocalDate(1920, 8, 5);
        Author author2 = new Author("Helmut Schmidt", author2Birthdate);

        bookService.save(new Book("1001", "Elasticsearch Basics", author1, LocalDate.now(),"zusammenfassung1"));
        bookService.save(new Book("1002", "Apache Lucene Basics", author1, LocalDate.now(),"zusammenfassung2"));
        bookService.save(new Book("1003", "Apache Solr Basics", author1, LocalDate.now(),"zusammenfassung3"));
        bookService
                .save(new Book("1004", "Harry Potter 1", author2, LocalDate.now().minusDays(15)));

//        final Book book = new Book("1005", "Apache Commons Basics 1997", author1, LocalDate.now());
//        book.setZusammenfassung("Schönes Buch über Apache Commons von 1997 mit Summary!!!!!!");
//        book.setIssue(new Issue("1997"));
//        bookService.save(book);

        //        final Iterable<Book> all = bookService.findAll();
        //        for (Book book : all) {
        //            bookService.delete(book);
        //        }

        //fuzzey search
        //        Page<Book> books = bookService.findByAuthor(author1, new PageRequest(0, 10));

        //List<Book> books = bookService.findByTitle("Elasticsearch Basics");

        //      books.forEach(x -> System.out.println(x));


        //BoolQueryBuilder builder = boolQuery();

        //        final BoolQueryBuilder birthdateQuery =
        //                boolQuery().must(rangeQuery("author.birthdate").lte(author2Birthdate))
        //                        .must(rangeQuery("author.birthdate").gte(author2Birthdate));
        //
        //        final NestedQueryBuilder authorQuery = nestedQuery("author", birthdateQuery);
        //
        //        result = bookService.search(authorQuery);


        //        System.out.println("Result for search for Helmut Schmidt:");
        //        result.forEach(x -> System.out.println(x));

        System.out.println("All Books:");
        result = bookService.findAll();
        result.forEach(x -> System.out.println(x));
        //        System.out.println("!!!!DELETE All Books!!!");
        //        result.forEach(x -> bookService.delete(x));
        //        System.out.println("All Books:");
        //        result = bookService.findAll();
        //        result.forEach(x -> System.out.println(x));

    }

    //useful for debug
    private void printElasticSearchInfo() {

        System.out.println("--ElasticSearch-->");
        Client client = es.getClient();

        Map<String, String> asMap = client.settings().getAsMap();

        asMap.forEach((k, v) -> {
            System.out.println(k + " = " + v);
        });
        System.out.println("<--ElasticSearch--");
    }

}