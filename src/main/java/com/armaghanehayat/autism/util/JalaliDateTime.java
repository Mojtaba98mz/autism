package com.armaghanehayat.autism.util;

import com.github.eloyzone.jalalicalendar.DateConverter;
import com.github.eloyzone.jalalicalendar.JalaliDate;
import java.time.*;
import java.time.format.DateTimeFormatter;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JalaliDateTime {

    private static final Logger log = LoggerFactory.getLogger(com.armaghanehayat.autism.util.JalaliDateTime.class);
    private static final ZoneId zoneId = ZoneId.of("Asia/Tehran");

    public static ZonedDateTime jalaliToGregorian(String jalaliDateTime) {
        try {
            DateConverter dateConverter = new DateConverter();
            String[] dateTimeList = jalaliDateTime.split(" ");
            String[] dateList = dateTimeList[0].split("-");
            String[] timeList = dateTimeList[1].split(":");
            LocalDate localDate = dateConverter.jalaliToGregorian(
                Integer.parseInt(dateList[0]),
                Integer.parseInt(dateList[1]),
                Integer.parseInt(dateList[2])
            );
            LocalDateTime localDateTime = localDate.atTime(Integer.parseInt(timeList[0]), Integer.parseInt(timeList[1]));
            return localDateTime.atZone(zoneId);
        } catch (Exception e) {
            log.error("Error in date conversion", e);
            return null;
        }
    }

    public static Instant jalaliToGregorianWithoutTime(String jalaliDate) {
        try {
            DateConverter dateConverter = new DateConverter();
            String[] dateList = jalaliDate.split("/");

            LocalDate localDate = dateConverter.jalaliToGregorian(
                Integer.parseInt(dateList[0]),
                Integer.parseInt(dateList[1]),
                Integer.parseInt(dateList[2])
            );
            LocalDateTime localDateTime = localDate.atTime(0, 0);
            localDateTime.atZone(zoneId);
            Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
            //            Instant instant = Instant.from(localDateTime);
            return instant;
        } catch (Exception e) {
            log.error("Error in date conversion", e);
            return null;
        }
    }

    public static ZonedDateTime jalaliToGregorianWithoutTimeEnd(String jalaliDate) {
        try {
            DateConverter dateConverter = new DateConverter();
            String[] dateList = jalaliDate.split("-");

            LocalDate localDate = dateConverter.jalaliToGregorian(
                Integer.parseInt(dateList[0]),
                Integer.parseInt(dateList[1]),
                Integer.parseInt(dateList[2])
            );
            LocalDateTime localDateTime = localDate.atTime(23, 59);
            return localDateTime.atZone(zoneId);
        } catch (Exception e) {
            log.error("Error in date conversion", e);
            return null;
        }
    }

    public static String adJalaliToGregorian(String jalaliDateTime) {
        try {
            DateConverter dateConverter = new DateConverter();
            String[] dateTimeList = jalaliDateTime.split(" ");
            String[] dateList = dateTimeList[0].split("-");
            String[] timeList = dateTimeList[1].split(":");
            LocalDate localDate = dateConverter.jalaliToGregorian(
                Integer.parseInt(dateList[0]),
                Integer.parseInt(dateList[1]),
                Integer.parseInt(dateList[2])
            );
            LocalDateTime localDateTime = localDate.atTime(Integer.parseInt(timeList[0]), Integer.parseInt(timeList[1]));
            //format is yyyy-MM-ddThh:mm
            return (
                localDateTime.getYear() +
                "-" +
                String.format("%02d", localDateTime.getMonthValue()) +
                "-" +
                String.format("%02d", localDateTime.getDayOfMonth()) +
                "T" +
                String.format("%02d", localDateTime.getHour()) +
                ":" +
                String.format("%02d", localDateTime.getMinute())
            );
        } catch (Exception e) {
            log.error("Error in date conversion", e);
            return null;
        }
    }

    public static String adGregorian(ZonedDateTime gregorian) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm").format(gregorian);
    }

    public static String gregorianToJalali(ZonedDateTime gregorian) {
        DateConverter dateConverter = new DateConverter();
        JalaliDate jalaliDate = dateConverter.gregorianToJalali(gregorian.getYear(), gregorian.getMonthValue(), gregorian.getDayOfMonth());
        return (
            jalaliDate.getDayOfWeek().getStringInPersian() +
            " " +
            String.format("%02d", jalaliDate.getDay()) +
            " " +
            jalaliDate.getMonthPersian().getStringInPersian() +
            " " +
            jalaliDate.getYear() +
            "-" +
            String.format("%02d", gregorian.getHour()) +
            ":" +
            String.format("%02d", gregorian.getMinute())
        );
    }

    public static Boolean isOverlapDates(ZonedDateTime startA, ZonedDateTime endA, ZonedDateTime startB, ZonedDateTime endB) {
        Interval interval = new Interval(startA.toEpochSecond(), endA.toEpochSecond());
        Interval interval2 = new Interval(startB.toEpochSecond(), endB.toEpochSecond());
        return interval.overlaps(interval2);
    }

    /**
     * convert seconds to human readable hours:minutes:seconds
     *
     * @param inputSeconds
     * @return
     */
    public static String convertSecondsToStringTime(long inputSeconds) {
        long seconds;
        long minutes;
        long hours;
        hours = inputSeconds / 3600;
        minutes = (inputSeconds % 3600) / 60;
        seconds = (inputSeconds % 3600) % 60;
        return hours + ":" + minutes + ":" + seconds;
    }

    public static String gregorianToJalaliWithoutPersianString(ZonedDateTime gregorian) {
        DateConverter dateConverter = new DateConverter();
        JalaliDate jalaliDate = dateConverter.gregorianToJalali(gregorian.getYear(), gregorian.getMonthValue(), gregorian.getDayOfMonth());
        return jalaliDate.toString();
    }

    public static JalaliDate gregorianToJalaliDate(ZonedDateTime gregorian) {
        DateConverter dateConverter = new DateConverter();
        JalaliDate jalaliDate = dateConverter.gregorianToJalali(gregorian.getYear(), gregorian.getMonthValue(), gregorian.getDayOfMonth());
        return jalaliDate;
    }
}
