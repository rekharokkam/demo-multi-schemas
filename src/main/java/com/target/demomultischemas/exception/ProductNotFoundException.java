package com.target.demomultischemas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ProductNotFoundException extends RuntimeException {

    private String productId;

    public ProductNotFoundException (String productId) {
        super ("Given productId : " + productId + " is not found in the system");
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }
}
