package com.chisom.accountservice.exception;

import org.springframework.http.HttpStatus;

/**
 * for general exceptions.
 *
 * @author Chisom.Iwowo
 */
public class CustomException extends RuntimeException {
    /**
     * For serialization: if any changes is made to this class, update the.
     * serialversionID
     */
    private static final long serialVersionUID = 1L;

    /**
     * the actual  message.
     */
    private final String message;

    /**
     * http status.
     */
    private final HttpStatus status;

    /**
     * cause of exception.
     */
    private Throwable cause;

    /**
     * constructor.
     *
     * @param msg        message
     * @param httpStatus status
     */
    public CustomException(final String msg,
                           final HttpStatus httpStatus) {
        this.message = msg;
        this.status = httpStatus;
    }

    /**
     * @param msg         message
     * @param actualCause cause of exception
     * @param httpStatus  statuc
     */
    public CustomException(final String msg,
                           final Throwable actualCause,
                           final HttpStatus httpStatus) {

        this.message = msg;
        this.cause = actualCause;
        this.status = httpStatus;
    }

    /**
     * get the message.
     *
     * @return String
     */
    @Override
    public String getMessage() {
        return this.message;
    }

    /**
     * returns the status.
     *
     * @return HttpStatus.
     */
    public HttpStatus getStatus() {
        return status;
    }

    /**
     * cause of exception.
     *
     * @return Throwable
     */
    @Override
    public synchronized Throwable getCause() {
        return cause;
    }
}
