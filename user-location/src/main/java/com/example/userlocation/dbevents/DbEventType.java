package com.example.userlocation.dbevents;

public enum DbEventType implements EventType {
    GET(0),
    POST(1),
    DELETE(2);

    private final int code;

    DbEventType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
