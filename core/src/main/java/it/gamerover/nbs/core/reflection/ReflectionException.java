package it.gamerover.nbs.core.reflection;

public class ReflectionException extends Exception {

    /**
     * Throw a reflection error.
     * @param errorMessage The error message.
     */
    public ReflectionException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Throw a reflection error followed by the throwable.
     * @param errorMessage The error message.
     * @param throwable The error.
     */
    public ReflectionException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }

}
