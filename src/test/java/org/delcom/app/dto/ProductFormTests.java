package org.delcom.app.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductFormTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validate_ValidForm_NoErrors() {
        ProductForm form = new ProductForm();
        form.setName("Laptop");
        form.setCategory("Elektronik");
        form.setPrice(10000.0);
        form.setStock(5);
        
        Set<ConstraintViolation<ProductForm>> violations = validator.validate(form);
        assertTrue(violations.isEmpty());
    }

    @Test
    void validate_InvalidForm_HasErrors() {
        // Branch: Validasi gagal (@NotBlank, @Min)
        ProductForm form = new ProductForm();
        form.setName(""); // Error: Blank
        form.setCategory(null); // Error: Null (jika pakai @NotBlank biasanya otomatis null check, tapi di kode Anda pakai @NotBlank)
        form.setPrice(-100.0); // Error: Min 0
        form.setStock(-1); // Error: Min 0
        
        Set<ConstraintViolation<ProductForm>> violations = validator.validate(form);
        
        // Kita mengharapkan 4 error (Name, Category, Price, Stock)
        // Catatan: @NotBlank pada category akan error jika null
        assertEquals(4, violations.size());
    }
}