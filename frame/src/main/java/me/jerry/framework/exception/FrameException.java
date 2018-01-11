package me.jerry.framework.exception;

/**
 * Created by Jerry on 2017/8/3.
 */

public class FrameException extends CustomException {
    public FrameException(String message) {
        super(message);
    }

    public FrameException(String message, Throwable cause) {
        super(message, cause);
    }

    public FrameException(Throwable cause) {
        super(cause);
    }
}
