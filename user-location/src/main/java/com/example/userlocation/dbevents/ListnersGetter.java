package com.example.userlocation.dbevents;

import java.util.Map;

public interface ListnersGetter<TListener, TTarget> {
    Map<TListener, TTarget> getListeners();
}
