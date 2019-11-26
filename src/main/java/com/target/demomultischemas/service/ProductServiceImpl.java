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

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private ProductRepo productRepo;
    private UserOrderRepo userOrderRepo;

    @Autowired
    public ProductServiceImpl (ProductRepo productRepo, UserOrderRepo userOrderRepo){
        this.productRepo = productRepo;
        this.userOrderRepo = userOrderRepo;
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

    /**
     * This is ONLY for testing transactions across multiple databases.
     * This is not recommended in actual production
     * @param product
     * @return
     */
    @Override
    public Product updatedProductName(Product product) {
        Product updatedProduct = productRepo.save(product);
        log.info("the updated product after name has been updated : " + updatedProduct);
        //get all orders of the product
        List<UserOrder> userOrdersOfAProduct = userOrderRepo.findByProductId(updatedProduct.getProductId());
        for (UserOrder userOrder: userOrdersOfAProduct) {
            userOrder.setProductName(updatedProduct.getProductName());
        }

        //Manually throwing error to simulate second update failure
        throw new NullPointerException("Manually Simulated");
//        List<UserOrder> updatedUserOrdersOfAProduct = userOrderRepo.saveAll(userOrdersOfAProduct);
//        return updatedProduct;
    }
}
