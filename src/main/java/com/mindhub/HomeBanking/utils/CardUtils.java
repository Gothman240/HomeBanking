package com.mindhub.HomeBanking.utils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public final class CardUtils {
    private CardUtils() {
    }
    public static int getCvv() {
        return (int)Math.floor(Math.random()*999-111)+111;
    }
    public static String getCardNumber() {
        return ((int)((Math.random() * (9999 - 1000))+1000))
                + "-" + ((int)((Math.random() * (9999 - 1000))+1000))
                + "-" + ((int)((Math.random() * (9999 - 1000))+1000))
                + "-" + ((int)((Math.random() * (9999 - 1000))+1000));
    }
    public static String getRandomAccountNumber() {
        return "VIN-" + String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999 + 1));
    }
    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }
}
