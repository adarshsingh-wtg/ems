package com.example.ems.exceptions;

public class DuplicateDepartmentNameException extends RuntimeException {
    public DuplicateDepartmentNameException(String message) {
        super(message);
    }
}
