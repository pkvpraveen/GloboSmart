package com.globosmart.productcalalogue.domain;

import java.util.ArrayList;
import java.util.List;

public class ProductBuilder {
    private String name;
    private String displayName;
    private String type;
    private List<String> countries = new ArrayList<>();

    public ProductBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder withDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public ProductBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public ProductBuilder withCountries(List<String> countries) {
        this.countries.addAll(countries);
        return this;
    }

    public Product build(){
        Product product = new Product();
        product.setName(name);
        product.setDisplayName(displayName);
        product.setType(type);
        countries.forEach(product::addCountries);
        return product;
    }
}
