package com.globosmart.productcalalogue.dao;

import com.globosmart.productcalalogue.utils.TestData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JdbcTest
public class CountryDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private CountryDao countryDao;

    @Before
    public void setUp() throws IOException {
        countryDao = new CountryDao(jdbcTemplate);
        new TestData(jdbcTemplate).givenProductsExist();
    }

    @Test
    public void shouldReturnAllCountries() {

        List<String> countries = countryDao.getAllCountries(newArrayList("US", "IN"));

        assertThat(countries).contains("US", "IN");
    }
}