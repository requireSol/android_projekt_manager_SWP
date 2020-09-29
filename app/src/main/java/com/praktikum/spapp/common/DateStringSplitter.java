package com.praktikum.spapp.common;

import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.TimeZone;

public class DateStringSplitter {

    public static String datePrettyPrint(String date){

        String[] arrayDate =  date.split("-");
        String year = arrayDate[0];
        String month = arrayDate[1];
        String day = arrayDate[2].substring(0, 2);

        return  Integer.parseInt(day) + "." + Integer.parseInt(month) + "." + year;
    }

    public static int yearPrettyPrint(String date){

        String[] arrayDate =  date.split("-");
        String year = arrayDate[0];
        String month = arrayDate[1];
        String day = arrayDate[2].substring(0, 2);

        return Integer.parseInt(year);
    }

    public static int monthPrettyPrint(String date){

        String[] arrayDate =  date.split("-");
        String year = arrayDate[0];
        String month = arrayDate[1];
        String day = arrayDate[2].substring(0, 2);

        return Integer.parseInt(month) - 1;
    }

    public static int dayPrettyPrint(String date){

        String[] arrayDate =  date.split("-");
        String year = arrayDate[0];
        String month = arrayDate[1];
        String day = arrayDate[2].substring(0, 2);

        return Integer.parseInt(day);
    }

    public static String timePrettyPrint(String date){

        String[] arryTime = date.split(":");
        String hour = arryTime[0].substring(11, 13);
        String minute = arryTime[1].substring(0, 2);

        return  hour + ":" + minute + " Uhr";
    }

    public static int hourPrettyPrint(String date){

        String[] arryTime = date.split(":");
        String hour = arryTime[0].substring(11, 13);

        return Integer.parseInt(hour);
    }

    public static int minutePrettyPrint(String date){

        String[] arryTime = date.split(":");
        String minute = arryTime[1].substring(0, 2);

        return Integer.parseInt(minute);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String changeToDateFormat(String date, String time, Context context){
        String[] arrayDate = date.split("\\.");
        String[] arrayTime = time.split(":");
        String year = arrayDate[2];
        String month = arrayDate[1];
        String[] monthLengthCheck = month.split("");
        String day = arrayDate[0];
        String[] dayLengthCheck = day.split("");

        String hour = arrayTime[0];
        String[] hourLengthCheck = hour.split("");
        String minute = arrayTime[1].substring(0, 2);
        String minuteReplaced = minute.replaceAll(" ","");
        String[] minuteLengthCheck = minuteReplaced.split("");
        String locale = context.getResources().getConfiguration().locale.getCountry();
        TimeZone tz = TimeZone.getDefault();

        LocalDateTime now = LocalDateTime.now();
        ZoneId zone = ZoneId.of(tz.getID());
        ZoneOffset zoneOffSet = zone.getRules().getOffset(now);
        String zoneOffset = zoneOffSet.toString();
        String checkDay = (dayLengthCheck.length == 1 ? "0" + day : day);
        String checkMonth = (monthLengthCheck.length == 1 ? "0" + month : month);
        String checkMinute = minuteLengthCheck.length == 1 ? "0" + minuteReplaced : minuteReplaced;
        String checkHour = (hourLengthCheck.length == 1 ? "0" + hour : hour);
        String timeISOFormat = year + "-" + checkMonth + "-" + checkDay + "T" + checkHour + ":" + checkMinute + ":00.000000" + zoneOffset;
        return timeISOFormat;
    }

}
