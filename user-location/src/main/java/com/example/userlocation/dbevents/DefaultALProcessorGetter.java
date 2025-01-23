package com.example.userlocation.dbevents;

import java.util.HashMap;
import java.util.Map;

public class DefaultALProcessorGetter extends DefaultAProcessorGetter<Listener, Class<?>>
        implements ListnersGetter<Listener, Class<?>> {
    public DefaultALProcessorGetter(Map<Listener, Class<?>> targets) {
        super(targets);
    }

    @Override
    protected Map<Listener, Class<?>> callback(Class<?> clazz) {
        var map = new HashMap<Listener, Class<?>>();
        if (clazz.isAnnotationPresent(Listener.class)) {
            var listener = clazz.getAnnotation(Listener.class);

            map.put(listener, clazz);
        }

        return map;
    }

    public Map<Listener, Class<?>> getListeners() {
        return targets;
    }
}
