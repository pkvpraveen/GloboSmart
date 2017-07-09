package com.globosmart.productcalalogue.services.validators;

import com.globosmart.productcalalogue.dao.CountryDao;
import com.globosmart.productcalalogue.domain.Product;
import com.globosmart.productcalalogue.domain.ProductBuilder;
import com.globosmart.productcalalogue.exceptions.CountryNotFoundException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CountryValidatorTest {

    @Mock
    private CountryDao countryDao;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldThrowExceptionIfCountryPotPresent() {
        when(countryDao.getAllCountries(anyListOf(String.class))).thenReturn(asList("US", "IN"));
        Product product = new ProductBuilder().withCountries(asList("US", "IN", "RU")).build();
        this.thrown.expect(CountryNotFoundException.class);
        this.thrown.expectMessage("[RU] not present");
        new CountryValidator(countryDao).validate(product);
    }


}