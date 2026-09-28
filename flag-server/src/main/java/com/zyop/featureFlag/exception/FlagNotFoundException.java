package com.zyop.featureFlag.exception;

public class FlagNotFoundException extends RuntimeException {
    public FlagNotFoundException(Long id) {
        super("Flag not found with id: " + id);
    }
}
