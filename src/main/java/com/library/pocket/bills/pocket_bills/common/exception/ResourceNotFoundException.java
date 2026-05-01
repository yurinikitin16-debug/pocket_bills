package com.library.pocket.bills.pocket_bills.common.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName) {
        super(resourceName + " not found");
    }
}
