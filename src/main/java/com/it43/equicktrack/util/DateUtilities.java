package com.it43.equicktrack.util;

import java.time.LocalDateTime;

public class DateUtilities {
    public static boolean isLate(LocalDateTime localDateTime){
        return LocalDateTime.now().isAfter(localDateTime.plusMinutes(Constant.LATE_RETURN_MINUTES));
    }

    public static boolean isPast(LocalDateTime localDateTime){
        return LocalDateTime.now().isAfter(localDateTime);
    }

}
