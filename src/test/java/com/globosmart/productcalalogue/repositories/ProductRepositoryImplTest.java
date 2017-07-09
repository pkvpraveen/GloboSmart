package com.globosmart.productcalalogue.repositories;

import com.globosmart.productcalalogue.dao.ProductDao;
import com.globosmart.productcalalogue.domain.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductRepositoryImplTest {

    @Mock
    private ProductDao productDao;

    private ProductRepository productRepository;

    @Before
    public void setUp() {
        productRepository = new ProductRepositoryImpl(productDao);
    }

    @Test
    public void shouldGetProduct() {
        ArrayList<Product> products = newArrayList();
        when(productDao.getProducts(anyMapOf(String.class, String.class))).thenReturn(products);
        assertEquals(products, productRepository.getProducts(Collections.emptyMap()));
    }

    @Test
    public void shouldAddProduct() {
        Product product = new Product();
        productRepository.addProduct(product);
        verify(productDao).addProduct(product);
        verify(productDao).mapProductToCountries(product);
    }

    @Test
    public void shouldGetProductById() {
        Product product = new Product();
        when(productDao.getProductById(1)).thenReturn(product);
        assertEquals(product, productRepository.getProductById(1));
    }
}