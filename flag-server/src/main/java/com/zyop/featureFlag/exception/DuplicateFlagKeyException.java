package com.zyop.featureFlag.exception;

public class DuplicateFlagKeyException extends RuntimeException {
    public DuplicateFlagKeyException(String key) {
        super("Flag with key '" + key + "' already exists");
    }
}
