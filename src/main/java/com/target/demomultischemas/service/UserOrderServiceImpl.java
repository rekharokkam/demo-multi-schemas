package com.target.demomultischemas.service;

import com.target.demomultischemas.entity.commerce.UserOrder;
import com.target.demomultischemas.entity.inventory.Product;
import com.target.demomultischemas.exception.CustomException;
import com.target.demomultischemas.exception.UserOrderNotFoundException;
import com.target.demomultischemas.repository.commerce.UserOrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserOrderServiceImpl implements UserOrderService {

    private ProductService productService;
    private UserOrderRepo userOrderRepo;

    @Autowired
    public UserOrderServiceImpl (ProductService productService, UserOrderRepo userOrderRepo) {
        this.productService = productService;
        this.userOrderRepo = userOrderRepo;
    }

    @Override
    public UserOrder createUserOrder(UserOrder userOrder) {
        Assert.notNull (userOrder.getProductId(), "ProductId is required to create an order");
        Assert.state((userOrder.getQuantity() > 0), "Quantity should be greater than 0");
        Product product = productService.getProduct(userOrder.getProductId());
        if (!product.getActive()){
            throw new CustomException("Order cannot be created for inactive product");
        }
        userOrder.setProductName(product.getProductName());
        return userOrderRepo.save(userOrder);
    }

    @Override
    public UserOrder getUserOrder(String orderId) {
        Assert.notNull(orderId, "OrderId cannot be null");

        Optional<UserOrder> userOrder = userOrderRepo.findById(orderId);
        if (!userOrder.isPresent()){
            throw new UserOrderNotFoundException(orderId);
        }
        UserOrder userOrder1 = userOrder.get();
        Product product = productService.getProduct(userOrder1.getProductId());
        userOrder1.setProductCode(product.getProductCode());
        return userOrder1;
    }

    @Override
    public List<UserOrder> getAllOrdersForAProduct(String productId) {
        List<UserOrder> userOrders = userOrderRepo.findByProductId(productId);
        if (null != userOrders && userOrders.size() > 0) {
            for (UserOrder userOrder: userOrders) {
                Product product = productService.getProduct(userOrder.getProductId());
                userOrder.setProductCode(product.getProductCode());
            }
        }
        return userOrders;
    }
}
