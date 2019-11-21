package com.target.demomultischemas.exception;

public class UserOrderNotFoundException extends RuntimeException {
    private String userOrderId;

    public UserOrderNotFoundException(String userOrderId) {
        super ("Given userOrderId : " + userOrderId + " is not found in the system");
        this.userOrderId = userOrderId;
    }

    public String getUserOrderId() {
        return userOrderId;
    }
}
