package org.crops.fitserver.global.http;

import java.util.List;

public record CursorResult<T>(
    List<T> values,
    Boolean hasNext) {

    public static <T> CursorResult<T> of(List<T> values, Boolean hasNext) {
        return new CursorResult<>(values, hasNext);
    }
}