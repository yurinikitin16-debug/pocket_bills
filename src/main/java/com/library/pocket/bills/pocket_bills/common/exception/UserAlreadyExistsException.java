package com.library.pocket.bills.pocket_bills.common.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super("User with this email already exists");
    }
}
