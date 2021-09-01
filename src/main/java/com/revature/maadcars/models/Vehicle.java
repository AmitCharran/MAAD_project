package com.revature.maadcars.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Foreign keys:
 * - Many to one: users
 * - Many to one: models
 * - One to many: sales
 */
@Entity
@Table(name="vehicles")
@Getter @Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Vehicle {
    @Id
    @Column(name = "vehicle_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int vehicle_id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "model_id", referencedColumnName = "model_id", nullable = false)
    private Model model;

    @JsonIgnore
    @OneToMany(mappedBy = "vehicle")
    private List<Sale> sales;

    @Column(name = "vin", unique = true, nullable = false)
    private String vin;

    @Column(name = "color")
    private String color;

    @Column(name = "is_stolen", columnDefinition = "boolean not null default false")
    private boolean is_stolen;

    @Column(name = "description")
    private String description;
}