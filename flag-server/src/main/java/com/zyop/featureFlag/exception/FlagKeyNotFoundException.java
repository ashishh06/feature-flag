package com.zyop.featureFlag.exception;

public class FlagKeyNotFoundException extends RuntimeException {
    public FlagKeyNotFoundException(String key) {
        super("Flag not found with key: " + key);
    }
}
