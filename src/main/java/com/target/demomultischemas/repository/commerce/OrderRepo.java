package com.target.demomultischemas.repository.commerce;

import com.target.demomultischemas.entity.commerce.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository <UserOrder, String> {
}
