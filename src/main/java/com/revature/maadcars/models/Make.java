package com.revature.maadcars.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Foreign keys:
 * - One to many: models
 */
@Entity
@Table(name="makes")
@Getter
@Setter
@NoArgsConstructor

public class Make {
    /**
     * TODO: Get model IDs from NHTSA API instead
     */
    @Id
    @Column(name = "make_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int make_id;

    @JsonIgnore
    @OneToMany(mappedBy = "make")
    private List<Model> models;

    @Column(name = "name", nullable = false)
    private String name;
}