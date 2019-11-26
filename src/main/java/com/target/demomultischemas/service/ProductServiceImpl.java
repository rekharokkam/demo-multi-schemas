package com.target.demomultischemas.service;

import com.target.demomultischemas.entity.commerce.UserOrder;
import com.target.demomultischemas.entity.inventory.Product;
import com.target.demomultischemas.exception.ProductNotFoundException;
import com.target.demomultischemas.repository.commerce.UserOrderRepo;
import com.target.demomultischemas.repository.inventory.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private ProductRepo productRepo;
    private UserOrderRepo userOrderRepo;

    @Autowired
    public ProductServiceImpl (ProductRepo productRepo){
        this.productRepo = productRepo;
    }

    @Override
    public Product updateProduct(Product product) {
        Product updatedProduct = productRepo.save(product);
        return updatedProduct;
    }

    @Override
    public Product getProduct(String productId) {
        Assert.notNull(productId, "ProductId cannot be null");
        return productRepo.findById(productId)
                .orElseThrow(() ->  new ProductNotFoundException(productId));
    }

    @Override
    public void deleteProduct(String productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        productRepo.delete(product);
    }

    @Override
    public Product createProduct(Product product) {
        return productRepo.save(product);
    }
}
