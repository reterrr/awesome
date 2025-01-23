package com.example.userlocation.dbevents;

import java.util.Map;

public interface EventAnnotationProcessorGetter<TEvent, TTarget> {
    Map<TEvent, TTarget> getTargets();
}
