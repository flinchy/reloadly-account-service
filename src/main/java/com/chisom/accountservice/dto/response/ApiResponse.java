package com.chisom.accountservice.dto.response;

import java.time.ZonedDateTime;

/**
 * @param <T>
 * @author Chisom.Iwowo
 */
public class ApiResponse<T> {

    /**
     * timestamp.
     */
    private final ZonedDateTime timestamp;

    /**
     * message.
     */
    private final String message;

    /**
     * status.
     */
    private final boolean status;

    /**
     * data.
     */
    private final T data;

    /**
     * constructor.
     *
     * @param actualData   data
     * @param msg          message
     * @param actualStatus status
     */
    public ApiResponse(T actualData, final String msg,
                       final boolean actualStatus) {
        timestamp = ZonedDateTime.now();
        this.status = actualStatus;
        this.message = msg;
        this.data = actualData;
    }

    /**
     * get the timestamp.
     *
     * @return ZonedDateTime
     */
    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * get the status.
     *
     * @return boolean
     */
    public boolean getStatus() {
        return status;
    }

    /**
     * get the data.
     *
     * @return T
     */
    public T getData() {
        return data;
    }

    /**
     * get the message.
     *
     * @return String
     */
    public String getMessage() {
        return message;
    }
}
