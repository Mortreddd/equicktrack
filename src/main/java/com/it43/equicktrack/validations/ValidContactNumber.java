package com.it43.equicktrack.validations;

import com.it43.equicktrack.sms.SmsService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ValidContactNumber implements ConstraintValidator<ContactNumber, String> {
    private SmsService smsService;
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return smsService.isValidContactNumber(s);
    }
}
