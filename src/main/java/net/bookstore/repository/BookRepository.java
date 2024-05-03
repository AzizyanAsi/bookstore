package net.bookstore.repository;

import net.bookstore.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE " +
            " (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) "
            + "AND (:author IS NULL OR LOWER(b.author.firstName) LIKE LOWER(CONCAT('%', :author, '%')))"
            + "AND (:genre IS NULL OR LOWER(b.genre.name) LIKE LOWER(CONCAT('%', :genre, '%'))) "
    )
    Page<Book> findAllBy(@Param("author") String author,
                                               @Param("genre") String genre,
                                               @Param("title") String title,
                                               Pageable pageable);

}
