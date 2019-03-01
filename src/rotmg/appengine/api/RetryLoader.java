package rotmg.appengine.api;

import org.osflash.OnceSignal;

import alde.flash.utils.Vector;

public interface RetryLoader {

	OnceSignal complete = new OnceSignal();

	void setMaxRetries(int param1);

	void setDataFormat(String param1);

	void sendRequest(String param1, Vector param2);

	boolean isInProgress();

}
