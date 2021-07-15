package com.target.demomultischemas.service;

import com.target.demomultischemas.entity.commerce.UserOrder;
import com.target.demomultischemas.entity.inventory.Product;
import com.target.demomultischemas.exception.ProductNotFoundException;
import com.target.demomultischemas.repository.commerce.UserOrderRepo;
import com.target.demomultischemas.repository.inventory.ProductRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private ProductRepo productRepo;

    @Autowired
    public ProductServiceImpl (ProductRepo productRepo){
        this.productRepo = productRepo;
    }

    /**
     * This is ONLY for testing transactions across multiple databases.
     * Manually exception is thrown to test transaction across multiple databases
     * This is not recommended in production
     * @param product
     * @return
     */
    @Override
    public Product updateProduct(Product product) {

        //update the basic product first
        Product currentProduct = productRepo.getOne(product.getProductId());
        if (! StringUtils.isEmpty(product.getProductName())) {
            currentProduct.setProductName(product.getProductName());
        }
        if (! StringUtils.isEmpty(product.getProductCode())) {
            currentProduct.setProductCode(product.getProductCode());
        }
        if (null != product.getActive()) {
            currentProduct.setActive(product.getActive());
        }
        Product updatedProduct = productRepo.save(currentProduct);

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
