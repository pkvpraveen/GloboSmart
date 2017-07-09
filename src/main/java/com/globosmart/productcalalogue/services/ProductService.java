package com.globosmart.productcalalogue.services;

import com.globosmart.productcalalogue.domain.Product;
import com.globosmart.productcalalogue.exceptions.ProductNotFoundException;
import com.globosmart.productcalalogue.repositories.ProductRepository;
import com.globosmart.productcalalogue.services.validators.Validator;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private List<Validator> validators;

    public List<Product> getProductsByType() {
        return productRepository.getProducts(new HashMap<>());
    }

    public List<Product> getProducts(Map<String, String> filters) {
        return productRepository.getProducts(filters);
    }

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProductsByType(String type) {
        return productRepository.getProducts(ImmutableMap.of("type", type));
    }

    public List<Product> getProductsByCountryCode(String countryCode) {
        return productRepository.getProducts(ImmutableMap.of("countryCode", countryCode));
    }

    public int addProduct(Product product) {
        validators.forEach(validator -> validator.validate(product));
        return productRepository.addProduct(product);
    }

    public Product getProductsById(int id) {
        Product product = productRepository.getProductById(id);
        if (product == null) {
            throw new ProductNotFoundException("No Product with id " + id);
        }
        return product;
    }

    @Resource(name = "validators")
    public void setValidators(List<Validator> validators) {
        this.validators = validators;
    }
}
