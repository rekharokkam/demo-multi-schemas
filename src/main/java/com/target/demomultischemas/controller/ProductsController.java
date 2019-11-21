package com.target.demomultischemas.controller;

import com.target.demomultischemas.entity.inventory.Product;
import com.target.demomultischemas.exception.ProductNotFoundException;
import com.target.demomultischemas.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping (path = "/products")
public class ProductsController {

    private ProductService productService;

    public ProductsController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping (path = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product getProduct (@PathVariable ("productId") String productId) {

        log.info("ProductId to search : {}", productId);
        Product product = productService.getProduct(productId);
        log.info("Product Found : " + product);
        return product;
    }

    @PutMapping (consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Product updateProduct (@RequestBody Product product, String uri) {
        log.info("Before Update : " + product);
        Product updatedProduct = productService.updateProduct(product);
        log.info("Updated Product : " + updatedProduct);
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
        return ResponseEntity.created(location).build();
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
