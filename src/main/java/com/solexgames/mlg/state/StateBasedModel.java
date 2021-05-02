package com.solexgames.mlg.state;

/**
 * @param <T> type parameter for the model's state enum
 * @param <V> type parameter for the model's player profile object
 */
public abstract class StateBasedModel<T, V> {

    public abstract void start();
    public abstract void end(V profile);

    public abstract T getState();

    public abstract void cleanup();

}
