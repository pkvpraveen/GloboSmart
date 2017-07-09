package com.globosmart.productcalalogue;

import com.globosmart.productcalalogue.dao.CountryDao;
import com.globosmart.productcalalogue.services.validators.CountryValidator;
import com.globosmart.productcalalogue.services.validators.NotNullValidator;
import com.globosmart.productcalalogue.services.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static java.util.Arrays.asList;

@SpringBootApplication
public class ProductCatalogApplication {



    public static void main(String[] args) {
        SpringApplication.run(ProductCatalogApplication.class, args);
    }


}
