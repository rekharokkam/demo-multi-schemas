package com.target.demomultischemas.service;

import com.target.demomultischemas.entity.commerce.UserOrder;
import com.target.demomultischemas.entity.inventory.Product;

import java.util.List;

public interface UserOrderService {

    UserOrder createUserOrder (UserOrder userOrder);
    UserOrder updateUserOrder (UserOrder userOrder);
    void updateProductInfo (Product productName);
    UserOrder getUserOrder (String orderId);
    List<UserOrder> getAllOrdersForAProduct (String productId);
}
