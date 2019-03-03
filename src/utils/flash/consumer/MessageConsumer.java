package utils.flash.consumer;

import rotmg.net.impl.Message;

import java.util.function.Consumer;

/**
 * A method that accepts a Message
 */
public class MessageConsumer<T extends Message> {

    Consumer<T> consumer;
    Runnable runnable;

    public MessageConsumer(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    public MessageConsumer(Runnable runnable) {
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