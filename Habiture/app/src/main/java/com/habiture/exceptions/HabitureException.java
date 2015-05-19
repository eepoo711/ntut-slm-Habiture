package com.habiture.exceptions;

/**
 * Created by Yeh on 2015/5/18.
 */
public class HabitureException extends Exception {
    public HabitureException() {
    }

    public HabitureException(String detailMessage) {
        super(detailMessage);
    }

    public HabitureException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public HabitureException(Throwable throwable) {
        super(throwable);
    }
}
