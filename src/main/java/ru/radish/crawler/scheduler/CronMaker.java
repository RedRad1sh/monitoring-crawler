package ru.radish.crawler.scheduler;

import java.util.Date;
import java.util.Calendar;

public class CronMaker {

    public static String everyTwoHourFromDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY)%2;
        String cronHourPart = hour + "-" + (24-hour+1)+"/2";
        int minutes = calendar.get(Calendar.MINUTE);
        String cron = String.format("0 %d %s * * *", minutes, cronHourPart);
        return cron;
    }

    public static String everyHourFromDate(Date date){
        int minutes = getMinutesDate(date);
        String cron = String.format("0 %d * * * *", minutes);
        return cron;
    }

    public static String everyHalfHourFromDate(Date date){
        int minutes = getMinutesDate(date);
        String cron = String.format("0 %d/30 * * * *", minutes);
        return cron;
    }

    public static String everyMinute(Date date){
        int minutes = getMinutesDate(date);
        String cron = String.format("* */1 * * * *", minutes);
        return cron;
    }

    private static int getMinutesDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

}
