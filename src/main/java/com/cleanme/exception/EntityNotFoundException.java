package com.cleanme.exception;

/**
 * Custom exception thrown when an entity is not found
 */
public class EntityNotFoundException extends RuntimeException {
    
    public EntityNotFoundException(String message) {
        super(message);
    }
    
    public EntityNotFoundException(String entityName, Object id) {
        super(String.format("%s with id '%s' not found", entityName, id));
    }
    
    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 