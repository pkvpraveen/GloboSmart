package com.globosmart.productcalalogue.services.validators;

import com.globosmart.productcalalogue.dao.CountryDao;
import com.globosmart.productcalalogue.domain.Product;
import com.globosmart.productcalalogue.exceptions.CountryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class CountryValidator implements Validator {

    private CountryDao countryDao;

    @Autowired
    public CountryValidator(CountryDao countryDao) {
        this.countryDao = countryDao;
    }

    @Override
    public void validate(Product product) {
        List<String> allCountries = countryDao.getAllCountries(product.getCountries());
        if (product.getCountries().size() != allCountries.size()) {
            List<String> missingCountries = product.getCountries().stream()
                    .filter(c -> !allCountries.contains(c))
                    .collect(toList());
            throw new CountryNotFoundException(missingCountries + " not present");
        }
    }
}
