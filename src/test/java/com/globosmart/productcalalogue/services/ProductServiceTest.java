package com.globosmart.productcalalogue.services;

import com.globosmart.productcalalogue.domain.Product;
import com.globosmart.productcalalogue.exceptions.CountryNotFoundException;
import com.globosmart.productcalalogue.exceptions.ProductNotFoundException;
import com.globosmart.productcalalogue.repositories.ProductRepository;
import com.globosmart.productcalalogue.services.validators.CountryValidator;
import com.globosmart.productcalalogue.services.validators.NotNullValidator;
import com.globosmart.productcalalogue.services.validators.Validator;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock
    private CountryValidator countryValidator;
    @Mock
    private NotNullValidator notNullValidator;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Before
    public void setUp() {
        productService.setValidators(asList(countryValidator, notNullValidator));
    }

    @Test
    public void shouldGetAllProducts() {
        ArrayList<Product> expectedProducts = new ArrayList<>();
        when(productRepository.getProducts(anyMapOf(String.class, String.class))).thenReturn(expectedProducts);

        List<Product> products = productService.getProductsByType();

        assertEquals(expectedProducts, products);
    }

    @Test
    public void shouldGetAllProductsByType() {
        ArrayList<Product> expectedProducts = new ArrayList<>();
        when(productRepository.getProducts(anyMapOf(String.class, String.class))).thenReturn(expectedProducts);

        List<Product> products = productService.getProductsByType("type");

        assertEquals(expectedProducts, products);
        verify(productRepository).getProducts(ImmutableMap.of("type", "type"));
    }

    @Test
    public void shouldGetAllProductsByCountryCode() {
        ArrayList<Product> expectedProducts = new ArrayList<>();
        when(productRepository.getProducts(anyMapOf(String.class, String.class))).thenReturn(expectedProducts);

        List<Product> products = productService.getProductsByCountryCode("countryCode");

        assertEquals(expectedProducts, products);
        verify(productRepository).getProducts(ImmutableMap.of("countryCode", "countryCode"));
    }

    @Test
    public void shouldGetProductById() {
        Product product = new Product();
        when(productRepository.getProductById(anyInt())).thenReturn(product);

        assertEquals(product, productService.getProductsById(1));
    }

    @Test(expected = ProductNotFoundException.class)
    public void shouldThrowExceptionIfNoProductFound() {
        productService.getProductsById(1);
    }

    @Test
    public void shouldAddProduct() {
        Product product = new Product();
        when(productRepository.addProduct(product)).thenReturn(1);
        productService.addProduct(product);

        verify(productRepository).addProduct(product);
    }

    @Test(expected = CountryNotFoundException.class)
    public void shouldValidateCountriesExist() {
        doThrow(new CountryNotFoundException("")).when(countryValidator).validate(any(Product.class));
        productService.addProduct(new Product());
    }

    @Test(expected = ProductNotFoundException.class)
    public void shouldValidateProductNotNull() {
        doThrow(new ProductNotFoundException("")).when(notNullValidator).validate(null);
        productService.addProduct(null);
    }
}