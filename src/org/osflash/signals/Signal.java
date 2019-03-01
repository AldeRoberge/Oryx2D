package org.osflash.signals;

import alde.flash.utils.consumer.SignalConsumer;

import java.util.Vector;

public class Signal<T> {

    public Vector<SignalConsumer<? super T>> listeners;

    public Signal() {
        this.listeners = new Vector<>();
    }

    public void add(SignalConsumer<? super T> t) {
        this.listeners.add(t);
    }

    public void dispatch(T t) {
        for (SignalConsumer<? super T> sf : this.listeners) {
            sf.dispatch(t);
        }
    }

    public void dispatch() {
        for (SignalConsumer sf : this.listeners) {
            sf.dispatch();
        }
    }

    public void addOnce(SignalConsumer onTextChanged) {
    }

    public void remove(SignalConsumer<? super T> onShowPackage) {
    }

    public void removeAll() {
    }
}
