package org.developer.exception;

public class XMLConversionException extends RuntimeException {
    public XMLConversionException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

}
