package me.jerry.framework.exception;

/**
 * Created by Jerry on 2017/8/3.
 */

public class AnnotationException extends FrameException {
    public AnnotationException(String message) {
        super(message);
    }

    public AnnotationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnnotationException(Throwable cause) {
        super(cause);
    }
}
