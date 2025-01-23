package com.example.userlocation.dbevents;

public class UnPinSignaller extends Signaller<UnPin> {
    public UnPinSignaller() {
        super(new UnPin()); // Instantiate a new UnPin flag
    }
}
