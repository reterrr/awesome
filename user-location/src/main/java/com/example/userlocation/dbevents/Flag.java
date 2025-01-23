package com.example.userlocation.dbevents;

public abstract class Flag {
    private boolean status;

    public synchronized void on() {
        status = true;
    }

    public synchronized void off() {
        status = false;
    }

    public synchronized boolean isExecuted() {
        return status;
    }
}
