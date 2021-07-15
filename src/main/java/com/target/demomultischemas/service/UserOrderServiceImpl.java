package com.target.demomultischemas.service;

import com.target.demomultischemas.entity.commerce.UserOrder;
import com.target.demomultischemas.entity.inventory.Product;
import com.target.demomultischemas.exception.CustomException;
import com.target.demomultischemas.exception.UserOrderNotFoundException;
import com.target.demomultischemas.repository.commerce.UserOrderRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class UserOrderServiceImpl implements UserOrderService {

    private UserOrderRepo userOrderRepo;
    private ProductService productService;

    @Autowired
    public UserOrderServiceImpl (UserOrderRepo userOrderRepo, ProductService productService) {
        this.userOrderRepo = userOrderRepo;
        this.productService = productService;
    }

    @Override
    public UserOrder createUserOrder(UserOrder userOrder) {
        Assert.notNull (userOrder.getProductId(), "ProductId is required to create an order");
        Assert.state((userOrder.getQuantity() > 0), "Quantity should be greater than 0");

        Product product = productService.getProduct(userOrder.getProductId());
        if (!product.getActive()){
            throw new CustomException("Order cannot be created for inactive product");
        }

        userOrder.setProductCode(product.getProductCode());
        userOrder.setProductName(product.getProductName());

        return userOrderRepo.save(userOrder);
    }

    public UserOrder updateUserOrder(UserOrder userOrder) {
        Assert.notNull (userOrder.getOrderId(), "OrderId is required to update an order");

        UserOrder currentUserOrder = userOrderRepo.getOne(userOrder.getOrderId());

        if (userOrder.getQuantity() > 0) { //This is not a practical use case as quantity does not get updated on an order
            currentUserOrder.setQuantity(userOrder.getQuantity());
        }
        if (StringUtils.isEmpty(userOrder.getProductName())) {

            Product product = productService.getProduct(userOrder.getProductId());
            if (!product.getActive()){
                throw new CustomException("Order cannot be updated for an inactive product");
            }

            currentUserOrder.setProductName(userOrder.getProductName());
        }

        return userOrderRepo.save(userOrder);
    }

    public void updateProductInfo(Product product) {

        if (! product.getActive()) {
            throw new CustomException("Orders of an Inactive product cannot be updated");
        }

        List<UserOrder> userOrdersOfAProduct = getAllOrdersForAProduct(product.getProductId());
        if (!CollectionUtils.isEmpty(userOrdersOfAProduct)) {
            log.info("found some products : {} : for the given productId {}",
                    userOrdersOfAProduct.size(), product.getProductId());
            for (UserOrder userOrder : userOrdersOfAProduct) {
                if (! StringUtils.isEmpty(product.getProductName())) {
                    userOrder.setProductName(product.getProductName());
                }
                if (! StringUtils.isEmpty(product.getProductCode())) {
                    userOrder.setProductCode(product.getProductCode());
                }

                userOrderRepo.save(userOrder);

                //Manually throwing error to simulate second update failure
//                throw new NullPointerException("Manually Simulated");
            }
        }
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
