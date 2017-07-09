package com.globosmart.productcalalogue.domain;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private int id;
    private String name;
    private String displayName;
    private String type;
    private List<String> countries = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void addCountries(String country) {
        this.countries.add(country);
    }
}
