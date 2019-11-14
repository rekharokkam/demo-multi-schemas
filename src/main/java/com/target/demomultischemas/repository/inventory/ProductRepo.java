package com.target.demomultischemas.repository.inventory;

import com.target.demomultischemas.entity.inventory.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository <Product, String> {

    Product findByProductName (String productName);
}