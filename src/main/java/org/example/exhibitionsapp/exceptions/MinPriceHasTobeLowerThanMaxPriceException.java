package org.example.exhibitionsapp.exceptions;

public class MinPriceHasTobeLowerThanMaxPriceException extends Exception{
    public MinPriceHasTobeLowerThanMaxPriceException(String message, Throwable cause) {
        super(message, cause);
    }
}
