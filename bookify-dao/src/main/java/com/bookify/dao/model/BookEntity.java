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
//    @Id
//    @GeneratedValue
//    @Column(name = "id", updatable = false, nullable = false)
    private static final long serialVersionUID = 1L;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "genre")
    private String genre;

    @Column(name = "description")
    private String description;

    @Column(name = "number_of_pages")
    private int  numberOfPages;

    @Column(name = "copies_available")
    private int  copiesAvailable;

    @Column(name = "book_url")
    private String  bookUrl;
    
    @Column(name = "locations", columnDefinition = "TEXT")
    private String locations;
    
    public String getLocations() {
      return locations;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_file_map", joinColumns = {
            @JoinColumn(name = "book_id")}, inverseJoinColumns = {@JoinColumn(name = "file_id")})
    private Set<FilesEntity> files;

    @ManyToMany(mappedBy = "books")
    @JsonIgnore
    private Set<UserEntity> users;
}