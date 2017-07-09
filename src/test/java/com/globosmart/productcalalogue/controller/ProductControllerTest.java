package com.globosmart.productcalalogue.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globosmart.productcalalogue.dao.CountryDao;
import com.globosmart.productcalalogue.domain.Product;
import com.globosmart.productcalalogue.services.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;
    private final ArrayList<Product> products = new ArrayList<>();
    private final Product product = new Product();

    @Before
    public void setUp() {
        product.setId(1);
        products.add(product);
        products.add(new Product());
        given(productService.getProducts(anyMapOf(String.class, String.class))).willReturn(products);
    }

    @Test
    public void shouldGetAllProducts() throws Exception {

        mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(product.getId())));
    }

    @Test
    public void shouldGetAllProductsByType() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "SOAP");
        mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON).params(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(product.getId())));

    }

    @Test
    public void shouldGetAllProductsByCountryCode() throws Exception {

        mockMvc.perform(get("/country/{countryCode}/products", "US").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(product.getId())));

    }

    @Test
    public void shouldGetAllProductsById() throws Exception {
        given(productService.getProductsById(anyInt())).willReturn(product);

        mockMvc.perform(get("/products/{id}", "1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(product.getId())));

    }

    @Test
    public void shouldAddProduct() throws Exception {

        given(productService.addProduct(any(Product.class))).willReturn(1);
        ObjectMapper objectMapper = new ObjectMapper();
        Product product = new Product();
        product.setName("PRODUCT");
        product.setType("OIL");
        product.setDisplayName("Display Name");
        product.addCountries("US");
        String json = objectMapper.writeValueAsString(product);
        MvcResult mvcResult = mockMvc.perform(post("/products", "US").contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andReturn();
        assertThat(mvcResult.getResponse().getHeader("Location"), is("/products/1"));

    }
}
