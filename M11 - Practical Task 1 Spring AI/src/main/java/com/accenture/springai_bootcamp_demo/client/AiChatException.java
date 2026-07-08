package com.accenture.springai_bootcamp_demo.client;

/**
 * Raised when the AI model cannot be reached or returns an unusable
 * response. Carries an HTTP-friendly status hint for the web layer.
 */
public class AiChatException extends RuntimeException {

    public AiChatException(String message) {
        super(message);
    }

    public AiChatException(String message, Throwable cause) {
        super(message, cause);
    }
}
