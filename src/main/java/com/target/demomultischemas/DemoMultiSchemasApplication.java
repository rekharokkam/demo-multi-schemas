package com.target.demomultischemas;

import com.target.demomultischemas.entity.commerce.UserOrder;
import com.target.demomultischemas.service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoMultiSchemasApplication implements CommandLineRunner {

    private UserOrderService userOrderService;

    @Autowired
    public DemoMultiSchemasApplication (UserOrderService userOrderService){
        this.userOrderService = userOrderService;
    }

    @Override
    public void run(String... args) throws Exception {
        UserOrder userOrder = new UserOrder();
        userOrder.setProductId("0de6a796-052d-11ea-8d71-362b9e155667");
        userOrder.setQuantity(2);

        userOrderService.createUserOrder(userOrder);
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoMultiSchemasApplication.class, args);
    }
}
