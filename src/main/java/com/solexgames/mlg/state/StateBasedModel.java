package com.solexgames.mlg.state;

/**
 * State based modelling
 *
 * @param <T> type parameter for the model's state enum
 */
public abstract class StateBasedModel<T> {

    public abstract void start();
    public abstract void end();

    public abstract T getState();

    public abstract void cleanup();

}
