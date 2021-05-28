package com.solexgames.mlg.state;

/**
 * Model to create an object with states
 * <p></p>
 *
 * @param <K> model's state enum
 * @param <V> model's profile object
 */

public interface StateBasedModel<K, V> {

    void start();
    void end(V profile);

    K getState();

    void cleanup();

}
