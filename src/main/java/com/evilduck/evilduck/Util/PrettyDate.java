package com.evilduck.evilduck.Util;

import org.joda.time.DateTime;

import static java.lang.String.format;

public class PrettyDate {

    private final DateTime dateTime;

    PrettyDate(final DateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return format("%d/%d/%d - %d:%d:%d.%d",
                dateTime.dayOfYear().get(),
                dateTime.monthOfYear().get(),
                dateTime.year().get(),
                dateTime.hourOfDay().get(),
                dateTime.minuteOfHour().get(),
                dateTime.secondOfMinute().get(),
                dateTime.millisOfSecond().get());
    }

}
