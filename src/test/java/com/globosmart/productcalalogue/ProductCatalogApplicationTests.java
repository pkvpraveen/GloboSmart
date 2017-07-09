package com.globosmart.productcalalogue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.globosmart.productcalalogue.domain.Product;
import com.globosmart.productcalalogue.domain.ProductBuilder;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/testdata.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/clear.sql")})
public class ProductCatalogApplicationTests {

    @Autowired
    private TestRestTemplate template;

    private final TypeReference<List<Product>> listValueTypeRef = new TypeReference<List<Product>>() {
    };
    private final TypeReference<Product> valueTypeRef = new TypeReference<Product>() {
    };
    private final String productName = "Product";

    @Test
    public void getAllProducts() throws Exception {
        List<Product> products = get("/products", listValueTypeRef);
        assertThat(products.stream().anyMatch(product -> product.getName().equals("LIFEBOY"))).isTrue();
        assertThat(products.stream().anyMatch(product -> product.getName().equals("LUX"))).isTrue();
        assertThat(products.stream().anyMatch(product -> product.getName().equals("OLIVE"))).isTrue();
    }

    @Test
    public void getProductsByType() throws Exception {
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("type", "SOAP");
        List<Product> products = get("/products", Collections.emptyMap(), listValueTypeRef, queryParams);
        assertThat(products.stream().anyMatch(product -> product.getName().equals("LIFEBOY"))).isTrue();
        assertThat(products.stream().anyMatch(product -> product.getName().equals("LUX"))).isTrue();
    }

    @Test
    public void getProductsByName() throws Exception {
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("name", "LIFEBOY");
        List<Product> products = get("/products", Collections.emptyMap(), listValueTypeRef, queryParams);
        assertThat(products.stream().anyMatch(product -> product.getName().equals("LIFEBOY"))).isTrue();
    }

    @Test
    public void getProductsByCountry() throws Exception {
        HashMap<String, String> pathParams = new HashMap<>();
        pathParams.put("countryCode", "US");
        List<Product> products = get("/country/{countryCode}/products", pathParams, listValueTypeRef);
        assertThat(products.stream().anyMatch(product -> product.getName().equals("LUX"))).isTrue();
        assertThat(products.stream().anyMatch(product -> product.getName().equals("OLIVE"))).isTrue();
    }

    @Test
    public void addProducts() throws IOException {
        ResponseEntity<String> response = post("/products", new ProductBuilder()
                .withName(productName)
                .withDisplayName("Product Display Name")
                .withType("OIL")
                .withCountries(singletonList("US")).build());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Product createdProduct = get(response.getHeaders().getLocation().toString(), valueTypeRef);
        assertThat(response.getHeaders().getLocation().toString()).contains("/products/" + createdProduct.getId());
        assertThat(createdProduct.getId()).isNotNull();
        assertThat(productName).isEqualTo(createdProduct.getName());
    }

    @Test
    public void noProductFound() throws IOException {
        ResponseEntity<String> responseEntity = get("/products/{id}", ImmutableMap.of("id", String.valueOf(-1)), Collections.emptyMap());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void noSupportedCountry() throws JsonProcessingException {
        ResponseEntity<String> response = post("/products", new ProductBuilder()
                .withName(productName)
                .withDisplayName("Product Display Name")
                .withType("OIL")
                .withCountries(singletonList("RU")).build());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.PRECONDITION_FAILED);
    }

    private ResponseEntity<String> post(String url, Product build) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(getProductJson(build), headers);

        return template.exchange(url, HttpMethod.POST, entity, String.class);
    }


    private String getProductJson(Product product) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(product);
    }

    private <T> T get(String url, TypeReference<T> typeReference) throws IOException {
        return get(url, Collections.emptyMap(), typeReference, Collections.emptyMap());
    }

    private <T> T get(String url, Map<String, String> pathParams, TypeReference<T> typeReference) throws IOException {
        return get(url, pathParams, typeReference, Collections.emptyMap());
    }

    private <T> T get(String url, Map<String, String> pathParams, TypeReference<T> typeReference, Map<String, String> requestParams) throws IOException {
        ResponseEntity<String> result = get(url, pathParams, requestParams);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(result.getBody(), typeReference);
    }

    private ResponseEntity<String> get(String url, Map<String, String> pathParams, Map<String, String> requestParams) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url);
        if (!requestParams.isEmpty()) {
            requestParams.forEach(uriComponentsBuilder::queryParam);
        }
        URI uri = uriComponentsBuilder
                .buildAndExpand(pathParams).toUri();
        return template.exchange(uri, HttpMethod.GET, entity, String.class);
    }
}
