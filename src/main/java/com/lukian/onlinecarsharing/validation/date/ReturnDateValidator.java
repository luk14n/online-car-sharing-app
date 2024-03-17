package com.lukian.onlinecarsharing.validation.date;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReturnDateValidator implements
        ConstraintValidator<ReturnDateConstraint, LocalDate> {
    public static final LocalDate DATE_NOW = LocalDate.now();
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    @Override
    public void initialize(ReturnDateConstraint returnDate) {
    }

    @Override
    public boolean isValid(LocalDate localDate,
                           ConstraintValidatorContext cxt) {
        String dateString = localDate.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
        return localDate != null
                && localDate.isAfter(DATE_NOW)
                && dateString.matches("\\d{4}-\\d{2}-\\d{2}");
    }
}
