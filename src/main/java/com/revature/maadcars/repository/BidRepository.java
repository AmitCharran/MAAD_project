package com.revature.maadcars.repository;

import com.revature.maadcars.models.Bid;
import com.revature.maadcars.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRepository extends JpaRepository<Bid, Integer> {
}
