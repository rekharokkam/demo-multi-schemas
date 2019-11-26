package com.target.demomultischemas.service;

import com.target.demomultischemas.entity.inventory.Product;

import java.util.Optional;

public interface ProductService {

    Product updateProduct (Product product);
    Product getProduct (String productId);
    void deleteProduct (String productId);
    Product createProduct (Product product);
    Product updatedProductName (Product product);
}
