package com.charter.rewardmanagement.exception;

/**
 * Exception thrown when a requested resource is not found.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Creates a new ResourceNotFoundException with a message.
     *
     * @param message error message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}