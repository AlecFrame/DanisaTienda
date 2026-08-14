package com.alecframe.danisatienda.utils;

import android.util.Log;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class UtilsD {
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.##");

    public static String decimalFormat(double number) {
        return DECIMAL_FORMAT.format(number);
    }

    public static String getFechaEntera(Instant fecha) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss")
                        .withLocale(Locale.getDefault())
                        .withZone(ZoneId.systemDefault());

        return formatter.format(fecha);
    }
    public static String getFecha(Instant fecha) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        .withLocale(Locale.getDefault())
                        .withZone(ZoneId.systemDefault());

        return formatter.format(fecha);
    }

    public static String getTime(Instant fecha) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("HH:mm:ss")
                        .withLocale(Locale.getDefault())
                        .withZone(ZoneId.systemDefault());

        return formatter.format(fecha);
    }
}
