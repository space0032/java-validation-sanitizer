package com.validator.validators;

import com.validator.result.ValidationError;

import java.time.LocalDate;

/**
 * Collection of cross-field validation rules.
 */
public class CrossValidators {

    /**
     * Validates that the start date is before the end date.
     * This is meant to be used with the fields() method.
     * 
     * @return a function that validates date ordering
     */
    public static ValidationError dateOrder(String startFieldName, LocalDate startDate, String endFieldName, LocalDate endDate) {
        if (startDate != null && endDate != null && !startDate.isBefore(endDate)) {
            return new ValidationError(
                startFieldName + "," + endFieldName,
                startFieldName + " must be before " + endFieldName,
                "dateOrder"
            );
        }
        return null;
    }
}
