package com.target.demomultischemas.controller;

import com.target.demomultischemas.entity.inventory.Product;
import com.target.demomultischemas.exception.ProductNotFoundException;
import com.target.demomultischemas.service.ProductService;
import com.target.demomultischemas.service.UserOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping (path = "/products")
public class ProductsController {

    private ProductService productService;
    private UserOrderService userOrderService;

    public ProductsController(ProductService productService, UserOrderService userOrderService){
        this.productService = productService;
        this.userOrderService = userOrderService;
    }

    @GetMapping (path = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product getProduct (@PathVariable ("productId") String productId) {

        log.info("ProductId to search : {}", productId);
        Product product = productService.getProduct(productId);
        log.info("Product Found : " + product);
        return product;
    }

    /**
     * This method is only for testing the transaction across multiple databases. This is a bad design
     * @param product
     * @param uri
     * @return
     */
    @PutMapping (path = "/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Product updateProduct (@PathVariable ("productId") String productId,
                                  @RequestBody Product product) {
        product.setProductId(productId);
        log.info("Before Update : " + product);
        Product updatedProduct = productService.updateProduct(product);
        log.info("Updated Product : " + updatedProduct);

        //update the product name in userOrders
        userOrderService.updateProductInfo(updatedProduct);
        return updatedProduct;
    }

    @DeleteMapping (path = "/{productId}")
    @ResponseStatus (HttpStatus.NO_CONTENT)
    public void deleteProduct (@PathVariable ("productId") String productId) {
        log.info("ProductId to be deleted : " + productId);
        productService.deleteProduct(productId);
    }

    @PostMapping (consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus (HttpStatus.CREATED)
    public ResponseEntity<Product> createProduct (@RequestBody Product product){
        log.info ("Product to be added : " + product);
        Product newProduct = productService.createProduct(product);
        log.info("Product After Created : " + newProduct);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{productId}")
                .buildAndExpand(newProduct.getProductId())
                .toUri();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.LOCATION, location.toString());
        headers.add("Content-Type", "application/json");
        ResponseEntity<Product> response = new ResponseEntity<>(newProduct, headers, HttpStatus.CREATED);
        return response;
    }

    @ExceptionHandler (value = DataIntegrityViolationException.class)
    private ResponseEntity<String> handleDataIntegrityViolationException (DataIntegrityViolationException ex){
        log.error("Error occurred while invoking some operation", ex);
        Throwable exception = ex;
        do {
            exception = exception.getCause();
        } while ((null != exception.getCause() ));
        return ResponseEntity.badRequest().body(exception.getMessage());
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
