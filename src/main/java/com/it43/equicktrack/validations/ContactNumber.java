package com.it43.equicktrack.validations;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidContactNumber.class)
public @interface ContactNumber {
    String message() default "Contact Number must be valid";
    int min() default 11;
}
