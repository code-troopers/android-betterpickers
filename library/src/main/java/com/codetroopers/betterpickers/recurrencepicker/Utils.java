/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codetroopers.betterpickers.recurrencepicker;

import android.content.Context;
import android.text.format.Time;

import java.util.Calendar;

public class Utils {

    private static final String TAG = "CalUtils";

    public static final int YEAR_MIN = 1970;
    public static final int YEAR_MAX = 2036;

    /**
     * Get first day of week as android.text.format.Time constant.
     *
     * @return the first day of week in android.text.format.Time
     */
    public static int getFirstDayOfWeek(Context context) {
        int startDay = Calendar.getInstance().getFirstDayOfWeek();

        if (startDay == Calendar.SATURDAY) {
            return Time.SATURDAY;
        } else if (startDay == Calendar.MONDAY) {
            return Time.MONDAY;
        } else {
            return Time.SUNDAY;
        }
    }

    /**
     * Get first day of week as java.util.Calendar constant.
     *
     * @return the first day of week as a java.util.Calendar constant
     */
    public static int getFirstDayOfWeekAsCalendar(Context context) {
        return convertDayOfWeekFromTimeToCalendar(getFirstDayOfWeek(context));
    }

    /**
     * Converts the day of the week from android.text.format.Time to java.util.Calendar
     */
    public static int convertDayOfWeekFromTimeToCalendar(int timeDayOfWeek) {
        switch (timeDayOfWeek) {
            case Time.MONDAY:
                return Calendar.MONDAY;
            case Time.TUESDAY:
                return Calendar.TUESDAY;
            case Time.WEDNESDAY:
                return Calendar.WEDNESDAY;
            case Time.THURSDAY:
                return Calendar.THURSDAY;
            case Time.FRIDAY:
                return Calendar.FRIDAY;
            case Time.SATURDAY:
                return Calendar.SATURDAY;
            case Time.SUNDAY:
                return Calendar.SUNDAY;
            default:
                throw new IllegalArgumentException("Argument must be between Time.SUNDAY and " +
                        "Time.SATURDAY");
        }
    }
}