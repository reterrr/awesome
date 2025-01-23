package com.example.userlocation.dbevents;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class DefaultEAProcessor
        extends EventAnnotationProcessor<
        Event,
        Listener,
        DefaultAEProcessorGetter,
        DefaultALProcessorGetter
        > {

    public DefaultEAProcessor() {
        super(
                new DefaultAEProcessorGetter(new HashMap<>()),
                new DefaultALProcessorGetter(new HashMap<>())
        );
    }
}
