package utils.osflash;

import utils.osflash.signals.Signal;

public class OnceSignal<T> extends Signal<T> {

    public boolean hasBeenDispatched;

    @Override
    public void dispatch(T o) {
        if (!this.hasBeenDispatched) {
            this.hasBeenDispatched = true;
            super.dispatch(o);
        }
    }
}
