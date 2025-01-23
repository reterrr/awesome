package com.example.userlocation.dbevents;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Event {
    String slug();

    DbEventType type();

    Class<? extends Signaller<? extends Flag>> signaller();
}
