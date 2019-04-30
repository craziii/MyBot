package com.evilduck.util;

import java.util.TimerTask;
import java.util.function.Function;

// TODO: Test this shit, trying to have a generic callback, code reduction, yadda yadda, blah blah blah...
public class CallbackTimer<T> extends TimerTask {

    private final Function<T, ?> function;
    private final T o;

    public CallbackTimer(final Function function,
                         final T o) {
        this.function = function;
        this.o = o;
    }

    @Override
    public void run() {
        function.apply(o);
    }

}
