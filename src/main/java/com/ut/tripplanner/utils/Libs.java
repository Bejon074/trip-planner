package com.ut.tripplanner.utils;

import com.ut.tripplanner.enums.ScheduleDay;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        int formatedTime = min;
        if (min >= 60) {
            formatedTime = (min / 60) * 100 + min % 60;
        }
        int remainderOne = timeInHourMinuteFormat % 100;
        int remainderTwo = formatedTime % 100;
        if (remainderOne + remainderTwo >= 60) {
            int returnValue = (timeInHourMinuteFormat / 100 + formatedTime / 100 + (remainderOne + remainderTwo) / 60) * 100 + (remainderOne + remainderTwo) % 60;
            return returnValue;
        } else {
            return timeInHourMinuteFormat + formatedTime;
        }
    }

    public static int substractTime(int timeInHourMinuteFormat, int min){
        try {
            int substractHour = min / 60;
            int substractMin = min % 60;

            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            String hour = "" + (timeInHourMinuteFormat / 100);
            String minute = "" + (timeInHourMinuteFormat % 100);
            Date date = format.parse(hour + ":" + minute);
            int resultMin = date.getMinutes() - substractMin;
            int resultHour = date.getHours() - substractHour;
            Time result = new Time(resultHour, resultMin, 0);

            return Integer.parseInt("" + result.getHours() + (result.getMinutes() < 10 ? "0"+ result.getMinutes(): result.getMinutes()));
        }catch (ParseException ex){
            System.out.println("Parsing exception: "+ex);
            return -1;
        }

    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344 * 1000;
        return dist;
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}
