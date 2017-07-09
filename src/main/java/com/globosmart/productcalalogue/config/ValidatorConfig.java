package com.globosmart.productcalalogue.config;

import com.globosmart.productcalalogue.dao.CountryDao;
import com.globosmart.productcalalogue.services.validators.CountryValidator;
import com.globosmart.productcalalogue.services.validators.NotNullValidator;
import com.globosmart.productcalalogue.services.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static java.util.Arrays.asList;

@Configuration
public class ValidatorConfig {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    public List<Validator> validators() {
        return asList(new CountryValidator(new CountryDao(jdbcTemplate)), new NotNullValidator());
    }
}

