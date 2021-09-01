package com.revature.maadcars.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Foreign keys:
 * - Many to one: sales
 */
@Entity
@Table(name="bids")
@Getter @Setter
@NoArgsConstructor
public class Bid {
    @Id
    @Column(name = "bid_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bid_id;

    @ManyToOne
    @JoinColumn(name = "sale_id", referencedColumnName = "sale_id", nullable = false)
    private Sale sale;
}
