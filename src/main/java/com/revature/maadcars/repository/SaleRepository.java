package com.revature.maadcars.repository;

import com.revature.maadcars.models.Sale;
import com.revature.maadcars.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {
}
