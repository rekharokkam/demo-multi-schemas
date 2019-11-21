package com.target.demomultischemas.entity.inventory;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table (name = "product")
public class Product {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO, generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column (name = "productid", length = 225)
    private String productId;

    @Column(name = "productname", nullable = false, length = 200, unique = true)
    private String productName;

    @Column(name = "productcode", nullable = false, length = 2, unique = true)
    private String productCode;

    @Column(name = "active", nullable = false, columnDefinition = "boolean default false")
    private Boolean active;

    public Product(String productName, String productCode, Boolean active) {
        this.productName = productName;
        this.productCode = productCode;
        this.active = active;
    }

    public Product () {}

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return productId.equals(product.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    @Override
    public String toString() {
        return "Product : {" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", productCode='" + productCode + '\'' +
                ", active='" + active + '\'' +
                '}';
    }
}
