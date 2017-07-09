package com.globosmart.productcalalogue.dao;

import com.google.common.collect.ImmutableMap;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CountryDao {
    private NamedParameterJdbcTemplate jdbcTemplate;

    public CountryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
    }


    public List<String> getAllCountries(List<String> countryCodes) {
        String sql = "SELECT COUNTRY_CODE FROM COUNTRY WHERE COUNTRY_CODE IN (:countryCodes)";
        return jdbcTemplate.queryForList(sql, ImmutableMap.of("countryCodes", countryCodes), String.class);
    }
}
