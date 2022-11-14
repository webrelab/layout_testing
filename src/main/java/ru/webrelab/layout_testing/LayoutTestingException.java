package ru.webrelab.layout_testing;

public class LayoutTestingException extends RuntimeException {

    public LayoutTestingException(final Throwable throwable) {
        super(throwable);
    }
    public LayoutTestingException(final String message) {
        super(message);
    }
}
