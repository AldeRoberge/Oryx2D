package alde.flash.utils.consumer;

import flash.events.Event;

import java.util.function.Consumer;

/**
 * A method that accepts an Event
 */
public class EventConsumer<T extends Event> {

    private Consumer<T> consumer;
    private Runnable runnable;

    public EventConsumer(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    public EventConsumer(Runnable runnable) {
        this.runnable = runnable;
    }

    public void accept(T event) {
        if (this.runnable != null) {
            this.runnable.run();
        } else {
            this.consumer.accept(event);
        }
    }

}