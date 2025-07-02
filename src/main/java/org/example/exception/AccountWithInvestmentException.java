package org.example.exception;

public class AccountWithInvestmentException extends RuntimeException {
    public AccountWithInvestmentException(String message) {
        super(message);
    }
}
