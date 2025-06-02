package com.example.ems.exceptions;

public class ReadOnlyDepartmentException extends RuntimeException {
    public ReadOnlyDepartmentException(String message) {
        super(message);
    }
}
