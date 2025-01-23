package com.example.userlocation.dbevents;

public class Signaller<Signal extends Flag> {
    private final Signal signal;

    public Signaller(Signal signal) {
        this.signal = signal;
    }

    public void on() {
        signal.on();
    }

    public void off() {
        signal.off();
    }

    public boolean isExecuted() {
        return signal.isExecuted();
    }
}
