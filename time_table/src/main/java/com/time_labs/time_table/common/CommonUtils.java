package com.time_labs.time_table.common;

import java.util.logging.Logger;

public class CommonUtils {
    public static final Logger TIME_LABS_LOGGER = Logger.getLogger("Time_labs");

    public static boolean isEmpty(String stringValue){
        return stringValue == null;
    }

    public static boolean isValidNumber(Integer intValue){ return intValue > 0;}
}
