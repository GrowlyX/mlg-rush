package com.solexgames.mlg.state;

/**
 * @param <T> model's state enum
 * @param <V> model's profile object
 */
public abstract class StateBasedModel<T, V> {

    public abstract void start();
    public abstract void end(V profile);

    public abstract T getState();

    public abstract void cleanup();

}
