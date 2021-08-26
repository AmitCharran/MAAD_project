package com.revature.maadcars.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


/**
 * Foreign keys:
 * - One to many: vehicles
 * - Many to one: makes
 */
@Entity
@Table(name="models")
@Getter
@Setter
@NoArgsConstructor
public class Model {
    /**
     * TODO: Get model IDs from NHTSA API instead
     */
    @Id
    @Column(name = "model_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int model_id;

    @OneToMany(mappedBy = "vehicle")
    private List<Vehicle> vehicles;

    @ManyToOne
    @JoinColumn(name = "make_id", referencedColumnName = "make_id", nullable = false)
    private Make make;

    @Column(name = "name", unique = true, nullable = false)
    private String name;
}
