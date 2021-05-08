package com.solexgames.mlg.state;

/**
 * Model to create an object with states
 * <p></p>
 *
 * @param <K> model's state enum
 * @param <V> model's profile object
 */
public abstract class StateBasedModel<K, V> {

    public abstract void start();
    public abstract void end(V profile);

    public abstract K getState();

    public abstract void cleanup();

}
