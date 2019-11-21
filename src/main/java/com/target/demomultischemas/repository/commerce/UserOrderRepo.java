package com.target.demomultischemas.repository.commerce;

import com.target.demomultischemas.entity.commerce.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOrderRepo extends JpaRepository <UserOrder, String> {

    List <UserOrder> findByProductId (String productId);
    List<UserOrder> findByProductName (String productName);
}
