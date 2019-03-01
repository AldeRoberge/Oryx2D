package alde.flash.utils.consumer;

import java.util.function.Consumer;

public class SignalConsumer<T> {

	Consumer<T> consumer;
	Runnable runnable;

	public SignalConsumer(Consumer<T> consumer) {
		this.consumer = consumer;
	}

	public SignalConsumer(Runnable runnable) {
		this.runnable = runnable;
	}

	public void dispatch(T event) {
		if (this.runnable != null) {
			this.runnable.run();
		} else {
			this.consumer.accept(event);
		}
	}

	public void dispatch() {
		if (this.runnable != null) {
			this.runnable.run();
		} else {
			this.consumer.accept(null);
		}
	}

}