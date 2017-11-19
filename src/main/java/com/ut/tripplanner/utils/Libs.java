package com.ut.tripplanner.utils;

import com.ut.tripplanner.enums.ScheduleDay;

import java.util.Calendar;
import java.util.Date;

public class Libs {

    public static int getTimeInHourMinuteFormat(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String minute = calendar.get(Calendar.MINUTE) < 10 ? "0"+calendar.get(Calendar.MINUTE) : ""+calendar.get(Calendar.MINUTE);
        return  Integer.parseInt("" + calendar.get(Calendar.HOUR_OF_DAY) + minute);
    }

    public static ScheduleDay getScheduleDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return ScheduleDay.values()[calendar.get(Calendar.DAY_OF_WEEK) - 1];
    }

    public static int addTime(int timeInHourMinuteFormat, int min){
        int formatedTime2 = min;
        if (min >= 60) {
            formatedTime2 = (min / 60) * 100 + min % 60;
        }
        int remainderOne = timeInHourMinuteFormat % 100;
        int remainderTwo = formatedTime2 % 100;
        if (remainderOne + remainderTwo >= 60) {
            int returnValue = (timeInHourMinuteFormat / 100 + formatedTime2 / 100 + (remainderOne + remainderTwo) / 60) * 100 + (remainderOne + remainderTwo) % 60;
            return returnValue;
        } else {
            return timeInHourMinuteFormat + formatedTime2;
        }
    }
}
