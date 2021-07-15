package com.target.demomultischemas.controller;

import com.target.demomultischemas.entity.commerce.UserOrder;
import com.target.demomultischemas.entity.inventory.Product;
import com.target.demomultischemas.exception.CustomException;
import com.target.demomultischemas.exception.ProductNotFoundException;
import com.target.demomultischemas.exception.UserOrderNotFoundException;
import com.target.demomultischemas.service.ProductService;
import com.target.demomultischemas.service.UserOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/userorders")
@Slf4j
public class UserOrdersController {

    private UserOrderService userOrderService;
    private ProductService productService;

    @Autowired
    public UserOrdersController (UserOrderService userOrderService, ProductService productService){
        this.userOrderService = userOrderService;
        this.productService = productService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserOrder> createUserOrder (@RequestBody UserOrder userOrder){
        log.info ("UserOrder to be added : " + userOrder);
        UserOrder newUserOrder = userOrderService.createUserOrder(userOrder);
        log.info("UserOrder After Created : " + newUserOrder);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{orderId}")
                .buildAndExpand(newUserOrder.getOrderId())
                .toUri();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.LOCATION, location.toString());
        headers.add("Content-Type", "application/json");
        ResponseEntity<UserOrder> response = new ResponseEntity<>(newUserOrder, headers, HttpStatus.CREATED);
        return response;
    }

    @GetMapping (path = "/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserOrder getUserOrder (@PathVariable ("orderId") String orderId) {
        log.info("orderId to search : {}", orderId);
        UserOrder userOrder = userOrderService.getUserOrder(orderId);
        log.info("UserOrder Found : " + userOrder);
        return userOrder;
    }

    @GetMapping (path = "/products/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserOrder> getAllUserOrdersForAProduct (@PathVariable ("productId") String productId) {
        log.info("productId to search : {}", productId);
        List<UserOrder> userOrders = userOrderService.getAllOrdersForAProduct(productId);
        log.info("UserOrders Found : " + userOrders);
        return userOrders;
    }

    @ExceptionHandler (value = CustomException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private String handleCustomException (CustomException ex){
        log.error("Error occurred while invoking some operation", ex);
        return ex.getMessage();
    }

    @ExceptionHandler (value = UserOrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private String handleProductNotFoundException (UserOrderNotFoundException ex){
        log.error("Error occurred while invoking some operation", ex);
        return ex.getMessage();
    }

    @ExceptionHandler (value = ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private String handleProductNotFoundException (ProductNotFoundException ex){
        log.error("Error occurred while invoking some operation", ex);
        return ex.getMessage();
    }

    @ExceptionHandler (value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private void handleException (Exception ex){
        log.error("Error occurred while invoking some operation", ex);
    }
}
