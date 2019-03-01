package com.obd.exceptions;

/**
 * Thrown when there is a "?" message.
 */
public class MisunderstoodCommandException extends ResponseException {

    public MisunderstoodCommandException() {
        super("?");
    }

}
