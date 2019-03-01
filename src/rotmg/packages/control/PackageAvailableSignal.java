package rotmg.packages.control;

import org.osflash.signals.Signal;

public class PackageAvailableSignal extends Signal {

    PackageAvailableSignal instance;

    public PackageAvailableSignal getInstance() {
        if (this.instance == null) {
            this.instance = new PackageAvailableSignal();
        }
        return this.instance;
    }
}
