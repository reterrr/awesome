package com.example.userlocation.dbevents;

import lombok.AllArgsConstructor;

import java.lang.reflect.Method;
import java.util.Map;

@AllArgsConstructor
public abstract class EventAnnotationProcessor<
        TEvent extends Event,
        TListener extends Listener,
        TEAProcessorGetter extends EventsGetter<TEvent, Method>,
        TLAProcessorGetter extends ListnersGetter<TListener, Class<?>>
        >

        implements ListnersGetter<TListener, Class<?>>,
        EventsGetter<TEvent, Method> {

    protected final TEAProcessorGetter teapGetter;
    protected final TLAProcessorGetter tlapGetter;

    @Override
    public Map<TEvent, Method> getEvents() {
        return teapGetter.getEvents();
    }

    @Override
    public Map<TListener, Class<?>> getListeners() {
        return tlapGetter.getListeners();
    }
}
