package org.developer.exception;

public class DownloadException extends RuntimeException {
    public DownloadException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
