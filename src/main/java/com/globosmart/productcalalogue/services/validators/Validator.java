package com.globosmart.productcalalogue.services.validators;

import com.globosmart.productcalalogue.domain.Product;

public interface Validator {
    void validate(Product product);
}
