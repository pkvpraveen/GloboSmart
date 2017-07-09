package com.globosmart.productcalalogue.controller;

import com.globosmart.productcalalogue.domain.Product;
import com.globosmart.productcalalogue.exceptions.CountryNotFoundException;
import com.globosmart.productcalalogue.exceptions.ProductNotFoundException;
import com.globosmart.productcalalogue.services.ProductService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {

    private ProductService productService;

    @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> getProducts(@RequestParam Map<String, String> allRequestParams) {
        return productService.getProducts(allRequestParams);
    }

    @GetMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product getProductsByType(@PathVariable(value = "id") Integer id) {
        return productService.getProductsById(id);
    }

    @GetMapping(value = "/country/{countryCode}/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> getProductsByCountryCode(@PathVariable(value = "countryCode") String countryCode) {
        return productService.getProducts(ImmutableMap.of("countryCode", countryCode));
    }

    @PostMapping(value = "/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addProducts(@RequestBody Product product) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Location", "/products/" + productService.addProduct(product));
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity handleNotFoundException(RuntimeException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        HashMap<String, Object> msg = getMessage(e, status);
        return new ResponseEntity<>(msg, status);
    }

    @ExceptionHandler(CountryNotFoundException.class)
    public ResponseEntity handleException(RuntimeException e) {
        HttpStatus status = HttpStatus.PRECONDITION_FAILED;
        HashMap<String, Object> msg = getMessage(e, status);
        return new ResponseEntity<>(msg, status);
    }

    private HashMap<String, Object> getMessage(RuntimeException e, HttpStatus status) {
        HashMap<String, Object> msg = new HashMap<>(2);
        msg.put("error", status.value());
        msg.put("message", e.getMessage());
        return msg;
    }
}
