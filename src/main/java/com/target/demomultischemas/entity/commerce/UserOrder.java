package com.target.demomultischemas.entity.commerce;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table (name = "user_order", schema = "commerce")
public class UserOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "orderid", length = 225)
    private String orderId;

    @Column(name = "productid", nullable = false, length = 225)
    private String productId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "productname", nullable = false)
    private String productName;

    @Transient
    private  String productCode;

    public UserOrder(String productId, String productName, String productCode, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
        this.productCode = productCode;
    }

    public UserOrder (String productId, int quantity){
        this.productId = productId;
        this.quantity = quantity;
    }

    public UserOrder() {}

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserOrder userOrder = (UserOrder) o;
        return orderId.equals(userOrder.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }

    @Override
    public String toString() {
        return "UserOrder{" +
                "orderId='" + orderId + '\'' +
                ", productId='" + productId + '\'' +
                ", quantity=" + quantity + '\'' +
                ", productName=" + productName + '\'' +
                ", productCode=" + productCode +
                '}';
    }
}
