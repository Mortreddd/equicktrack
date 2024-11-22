package com.it43.equicktrack.validations;

import com.it43.equicktrack.contact.ContactService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidContactNumber implements ConstraintValidator<ContactNumber, String> {

    private final ContactService contactService;

    @Override
    public boolean isValid(String contactNumber, ConstraintValidatorContext context) {
        // Check for null or empty values
        if (contactNumber == null || contactNumber.isEmpty()) {
            return false;
        }

        return contactService.isValidContactNumber(contactNumber);
    }
}
