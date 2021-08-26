package com.revature.maadcars.services;

import com.revature.maadcars.models.Sale;
import com.revature.maadcars.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleService {
    private final SaleRepository saleRepository;

    @Autowired
    public SaleService(SaleRepository saleRepository){
        this.saleRepository = saleRepository;
    }

    public Sale saveSale(Sale sale){
        return saleRepository.save(sale);
    }

    public Sale getSaleBySaleId(Integer id){
        return saleRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public List<Sale> getAllSales(){
        return saleRepository.findAll();
    }

    public void deleteSale(Integer saleId){
        saleRepository.findById(saleId).ifPresent(saleRepository::delete);
    }
}
