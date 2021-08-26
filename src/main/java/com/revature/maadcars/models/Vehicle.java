package com.revature.maadcars.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="vehicles")
@Getter @Setter
@NoArgsConstructor
public class Vehicle {
    @Id
    @Column(name = "vehicle_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int vehicle_id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "vin", unique = true, nullable = false)
    private String vin;

    @Column(name = "color")
    private String color;

    @Column(name = "isStolen")
    private boolean isStolen;

    @Column(name = "description")
    private String description;
}
