package com.example.userlocation.dbevents;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DefaultAEProcessorGetter extends DefaultAProcessorGetter<Event, Method>
        implements EventsGetter<Event, Method> {
    public DefaultAEProcessorGetter(Map<Event, Method> targets) {
        super(targets);
    }

    protected Map<Event, Method> callback(Class<?> clazz) {
        var map = new HashMap<Event, Method>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Event.class)) {
                Event event = method.getAnnotation(Event.class);

                map.put(event, method);
            }
        }

        return map;
    }

    @Override
    public Map<Event, Method> getEvents() {
        return targets;
    }
}
