package flash.utils.timer;

import java.util.TimerTask;

import flash.consumer.EventConsumer;
import flash.events.EventDispatcher;
import flash.events.TimerEvent;

/**
 * Representing AS3's Timer
 */
public class Timer extends EventDispatcher {

    public int delay;
    TimerTask t = null;
    private int repeatCount;

    /**
     * Timer (is not started on Constructor, use start(int delay)
     */
    public Timer(int delay) {
        this(delay, -1);
    }

    public Timer(int delay, int repeatCount) {
        this.delay = delay;
        this.repeatCount = repeatCount;
    }

    public void start() {
        this.start(this.delay);
    }

    private void start(int delay) {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        if (Timer.this.repeatCount != -1) {
                            Timer.this.repeatCount--;
                        }

                        for (EventConsumer r : Timer.this.listeners.keySet()) {
                            if (Timer.this.listeners.get(r).equals(TimerEvent.TIMER)) {
                                r.accept(null);
                            } else if (Timer.this.listeners.get(r).equals(TimerEvent.TIMER_COMPLETE) && (Timer.this.repeatCount == 0)) {
                                r.accept(null);
                            }
                        }

                        if (Timer.this.repeatCount == 0) {
                            Timer.this.stop();
                        } else {
                            Timer.this.start(delay); //re-schedule (delay can be set dynamically)
                        }

                    }
                },
                delay
        );
    }

    public boolean stop() {
        return ((this.t != null) && this.t.cancel());
    }
}
