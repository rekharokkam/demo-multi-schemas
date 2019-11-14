package com.target.demomultischemas.entity.commerce;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table (name = "user_order", schema = "commerce")
public class UserOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "orderid", length = 225)
    private String orderid;

    @Column(name = "productid", nullable = false, length = 225)
    private String productId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "productname", nullable = false)
    private String productName;

    public UserOrder(String productId, String productName, String productCode, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
    }

    public UserOrder() {}

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserOrder userOrder = (UserOrder) o;
        return orderid.equals(userOrder.orderid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderid);
    }

    @Override
    public String toString() {
        return "UserOrder{" +
                "orderid='" + orderid + '\'' +
                ", productId='" + productId + '\'' +
                ", quantity=" + quantity + '\'' +
                ", productName=" + productName +
                '}';
    }
}
