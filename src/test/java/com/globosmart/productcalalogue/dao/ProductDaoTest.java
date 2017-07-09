package com.globosmart.productcalalogue.dao;

import com.globosmart.productcalalogue.domain.ProductBuilder;
import com.globosmart.productcalalogue.utils.TestData;
import com.globosmart.productcalalogue.domain.Product;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@JdbcTest
public class ProductDaoTest {

    private HashMap<String, String> filters;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ProductDao productDao;

    @Before
    public void setUp() throws IOException {
        filters = new HashMap<>();
        productDao = new ProductDao(jdbcTemplate);
        new TestData(jdbcTemplate).givenProductsExist();
    }

    @Test
    public void shouldGetAllProducts() {
        List<Product> products = productDao.getProducts(filters);

        assertThat(products.size(), is(3));
        assertTrue(products.stream().anyMatch(product -> product.getName().equals("LIFEBOY")));
        assertTrue(products.stream().anyMatch(product -> product.getName().equals("LUX")));
        assertTrue(products.stream().anyMatch(product -> product.getName().equals("OLIVE")));
    }

    @Test
    public void shouldGetProductsByProductType() {
        filters.put("type", "SOAP");
        List<Product> products = productDao.getProducts(filters);

        assertThat(products.size(), is(2));
        assertTrue(products.stream().anyMatch(product -> product.getName().equals("LIFEBOY")));
        assertTrue(products.stream().anyMatch(product -> product.getName().equals("LUX")));
    }

    @Test
    public void shouldGetProductsByCountry() {
        filters.put("countryCode", "US");
        List<Product> products = productDao.getProducts(filters);

        assertThat(products.size(), is(2));
        assertTrue(products.stream().anyMatch(product -> product.getName().equals("LUX")));
        assertTrue(products.stream().anyMatch(product -> product.getName().equals("OLIVE")));
    }

    @Test
    public void shouldGetProductsByTypeAndCountry() {
        filters.put("countryCode", "US");
        filters.put("type", "OIL");
        List<Product> products = productDao.getProducts(filters);

        assertThat(products.size(), is(1));
        assertTrue(products.stream().anyMatch(product -> product.getName().equals("OLIVE")));
    }

    @Test
    public void shouldGetProductById() {
        int anyId = productDao.getProducts(filters).stream().findAny().get().getId();
        Product product = productDao.getProductById(anyId);
        assertThat(product.getId(), is(anyId));
    }

    @Test
    public void shouldGetNullIfNoProduct() {
        assertNull(productDao.getProductById(0));
    }

    @Test
    public void shouldAddProduct() {
        Product product = new ProductBuilder()
                .withName("PRODUCT")
                .withType("OIL")
                .withDisplayName("Display Name")
                .withCountries(Collections.singletonList("US")).build();

        int id = productDao.addProduct(product);
        productDao.mapProductToCountries(product);

        List<Product> products = productDao.getProducts(ImmutableMap.of("id", String.valueOf(id)));
        assertThat(products.size(), is(1));
        Product actual = products.stream().findFirst().orElse(null);
        assertThat(actual.getName(), is(product.getName()));
        assertThat(actual.getDisplayName(), is(product.getDisplayName()));
        assertThat(actual.getType(), is(product.getType()));
        assertThat(actual.getCountries(), is(product.getCountries()));
    }
}