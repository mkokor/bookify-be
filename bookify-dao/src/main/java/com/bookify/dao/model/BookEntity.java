package com.bookify.dao.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "books")
public class BookEntity extends Auditable implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "issue_date", nullable = false)
    private LocalDateTime issueDate;

    @Column(name = "genre")
    private String genre;

    @Column(name = "description")
    private String description;


    @Column(name = " cover_image")
    private String  cover_image;

    @Column(name = "number_of_pages")
    private int  numberOfPages;

    @Column(name = "copies_available")
    private int  copiesAvailable;

    @Column(name = "book_url")
    private String  bookUrl;



    @ManyToMany(mappedBy = "books")
    @JsonIgnore
    private Set<UserEntity> users;
}