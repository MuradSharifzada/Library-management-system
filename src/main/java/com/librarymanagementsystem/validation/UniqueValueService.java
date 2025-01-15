package com.librarymanagementsystem.validation;

public interface UniqueValueService {
    boolean isValueUnique(String value, Class<?> entityType);
}