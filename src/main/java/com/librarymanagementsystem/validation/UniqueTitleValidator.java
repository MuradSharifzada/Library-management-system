package com.librarymanagementsystem.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueTitleValidator implements ConstraintValidator<UniqueTitle, String> {

    private final UniqueValueService uniqueValueService;
    private Class<?> entityType;

    @Override
    public void initialize(UniqueTitle constraintAnnotation) {
        this.entityType = constraintAnnotation.entity();
    }

    @Override
    public boolean isValid(String title, ConstraintValidatorContext context) {
        if (title == null || title.trim().isEmpty()) {
            return true;
        }
        return uniqueValueService.isValueUnique(title, entityType);
    }
}
