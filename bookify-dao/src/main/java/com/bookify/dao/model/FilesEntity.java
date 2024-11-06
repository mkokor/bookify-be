package com.bookify.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "files")
public class FilesEntity extends Auditable implements Serializable  {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = " cover_image")
    private String  cover_image;

    @Column(name = "path")
    private String  path;


    @ManyToMany(mappedBy = "files")
    @JsonIgnore
    private Set<BookEntity> books;

}
