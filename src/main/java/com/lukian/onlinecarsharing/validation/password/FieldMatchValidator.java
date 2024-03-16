package com.lukian.onlinecarsharing.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(final FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        try {
            final Object firstObj = org.apache.commons.beanutils.BeanUtils
                    .getProperty(value, firstFieldName);
            final Object secondObj = org.apache.commons.beanutils.BeanUtils
                    .getProperty(value, secondFieldName);

            return firstObj == null && secondObj == null
                    || firstObj != null && firstObj.equals(secondObj);
        } catch (final Exception ignore) {
            //ignore
        }
        return true;
    }
}
