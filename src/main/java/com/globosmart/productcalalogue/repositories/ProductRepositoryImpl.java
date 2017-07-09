package com.globosmart.productcalalogue.repositories;

import com.globosmart.productcalalogue.dao.ProductDao;
import com.globosmart.productcalalogue.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private ProductDao productDao;

    public ProductRepositoryImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public List<Product> getProducts(Map<String, String> filters) {
        return productDao.getProducts(filters);
    }

    @Override
    public int addProduct(Product product) {
        product.setId(productDao.addProduct(product));
        productDao.mapProductToCountries(product);
        return product.getId();
    }

    @Override
    public Product getProductById(int id) {
        return productDao.getProductById(id);
    }
}
