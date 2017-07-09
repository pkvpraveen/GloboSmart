package com.globosmart.productcalalogue.repositories;

import com.globosmart.productcalalogue.domain.Product;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface ProductRepository {
    List<Product> getProducts(Map<String,String> filters);
    @Transactional
    int addProduct(Product product);
    Product getProductById(int id);
}
