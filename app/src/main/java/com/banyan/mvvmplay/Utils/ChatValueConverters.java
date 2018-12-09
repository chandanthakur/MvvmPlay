package com.banyan.mvvmplay.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ChatValueConverters {

    public static String getIsoDateInFormat(String dateString, String outFormat) {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat ft = new SimpleDateFormat(outFormat, Locale.US);
        return ft.format(date);
    }
}