package org.example.exhibitionsapp.exceptions;

public class FailedToAddExhibitionException extends Exception{
    public FailedToAddExhibitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
