package com.revature.maadcars.repository;

import com.revature.maadcars.models.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {
}
