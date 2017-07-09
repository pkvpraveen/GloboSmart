package com.globosmart.productcalalogue.services.validators;

import com.globosmart.productcalalogue.domain.Product;
import com.globosmart.productcalalogue.exceptions.ProductNotFoundException;

public class NotNullValidator implements Validator {
    @Override
    public void validate(Product product) {
        if (product == null) {
            throw new ProductNotFoundException("Product is required");
        }
    }
}
