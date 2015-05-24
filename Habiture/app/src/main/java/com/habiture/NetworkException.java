package com.habiture;

/**
 * Created by Yeh on 2015/5/24.
 */
public class NetworkException extends RuntimeException{

    public NetworkException() {
    }

    public NetworkException(String detailMessage) {
        super(detailMessage);
    }

    public NetworkException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NetworkException(Throwable throwable) {
        super(throwable);
    }
}
