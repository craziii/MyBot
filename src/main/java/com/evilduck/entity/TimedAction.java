package com.evilduck.entity;

/*
 * Lol, this is just Function<T, R> with no R
 *
 * */
public interface TimedAction<T> {

    // He returns NOTHING!
    void apply(T t);

}
