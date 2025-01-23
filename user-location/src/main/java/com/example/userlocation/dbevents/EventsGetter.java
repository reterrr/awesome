package com.example.userlocation.dbevents;

import java.util.Map;

public interface EventsGetter<TEvent, TTarget> {
    Map<TEvent, TTarget> getEvents();
}
