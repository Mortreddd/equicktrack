package com.it43.equicktrack.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtilities {
    public static boolean isLate(LocalDateTime localDateTime){
        return LocalDateTime.now().isAfter(localDateTime.plusMinutes(Constant.LATE_RETURN_MINUTES));
    }

    public static boolean isPast(LocalDateTime localDateTime){
        return LocalDateTime.now().isAfter(localDateTime);
    }

//    Date string to format: 9/21/2024, 9:35 PM
    public static LocalDateTime parseDateTimeString(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy, h:mm a");
        return LocalDateTime.parse(dateTimeString, formatter);
    }

}
