package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.TimeZone;

public class DateConverter {

    public synchronized static Long convertLocalDateTimeToLong(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public synchronized static String convertTimeMillisToStringFormat(Long timeMillis) {
        return convertTimeMillisToStringFormat(timeMillis, "UTC");
    }

    public synchronized static String convertTimeMillisToStringFormat(Long timeMillis, String timeZone) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        Date date = new Date(timeMillis);

        return dateFormat.format(date);
    }

    public synchronized static LocalTime convertStringToTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

        return LocalTime.parse(time, formatter);
    }

    public synchronized static LocalDateTime convertStringToDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

        return LocalDateTime.parse(date, formatter);
    }

    public synchronized static String convertLocalTimeToAmericanTime(LocalTime localTime) {
        return LocalTime.parse(localTime.toString(), DateTimeFormatter.ofPattern("HH:mm")).format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

    public synchronized static String convertLocalTimeToAmericanTime(LocalDateTime localDateTime) {
        return LocalDateTime.parse(localDateTime.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")).format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a"));
    }

    public synchronized static String convertLocalTimeToAmericanDate(LocalDateTime localDateTime) {
        return LocalDateTime.parse(localDateTime.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")).format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    public synchronized static String convertLocalDateTimeToStringTime(LocalDateTime localDateTime) {
        return LocalDateTime.parse(localDateTime.truncatedTo(ChronoUnit.MINUTES).toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")).format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
    }

    public synchronized static LocalDateTime convertMillisToLocalDateTime(Long timeMillis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), ZoneId.systemDefault());
    }

    public synchronized static LocalDateTime convertMillisToZonedLocalDateTime(Long timeMillis, String timezone) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), ZoneId.of(timezone));
    }
}
