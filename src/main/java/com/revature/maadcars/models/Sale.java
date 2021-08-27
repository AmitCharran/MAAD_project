package com.revature.maadcars.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Time;
import java.util.List;

/**
 * Foreign keys:
 * - Many to one: vehicles
 * - One to many: bids
 */
@Entity
@Table(name="sales")
@Getter @Setter
@NoArgsConstructor
public class Sale {
    @Id
    @Column(name = "sale_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sale_id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "vehicle_id")
    private Vehicle vehicle;

    @OneToMany(mappedBy = "sale")
    private List<Bid> bids;

    @Column(name = "time_started")
    private Time time_started;
}
