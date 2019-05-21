package com.evilduck.util;

import com.evilduck.entity.TimedAction;

import java.util.Timer;
import java.util.TimerTask;

import static org.joda.time.DateTime.now;

// TODO: Test this shit, trying to have a generic callback, code reduction, yadda yadda, blah blah blah...
public class CallbackTimer<T> extends TimerTask {

    private final Timer timer;
    private final TimedAction<T> timedAction;
    private final T o;

    public CallbackTimer(final Timer timer,
                         final TimedAction<T> timedAction,
                         final T o) {
        this.timer = timer;
        this.timedAction = timedAction;
        this.o = o;
    }

    private CallbackTimer copy() {
        return new CallbackTimer(timer, timedAction, o);
    }

    public void schedule(final int seconds) {
        timer.schedule(copy(), now().plusSeconds(seconds).toDate());
    }

    @Override
    public void run() {
        timedAction.apply(o);
    }

}
