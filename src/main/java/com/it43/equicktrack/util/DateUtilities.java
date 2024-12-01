package com.it43.equicktrack.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtilities {

    public static boolean isLate(LocalDateTime localDateTime){
        return now().isAfter(localDateTime.plusMinutes(Constant.OTP_EXPIRATION));
    }

    public static boolean isPast(LocalDateTime localDateTime){
        return now().plusMinutes(Constant.LATE_RETURN_MINUTES).isAfter(localDateTime);
    }

    public static LocalDateTime now() {
        ZoneId manilaZoneId = ZoneId.of("Asia/Manila");
        return LocalDateTime.now(manilaZoneId);
    }

    public static boolean isExpired(LocalDateTime localDateTime) {
        return now().isAfter(localDateTime.plusMinutes(Constant.OTP_EXPIRATION));
    }

    public static boolean isEnding(LocalDateTime localDateTime) {
        return now().isAfter(localDateTime.minusMinutes(Constant.BEFORE_RETURN_MINUTES));
    }

}
