package com.mindhub.HomeBanking.utils;


public final class CardUtils {
    private CardUtils() {
    }
    public static int getCvv() {
        return (int)Math.floor(Math.random()*999-111)+111;
    }
    public static String getCardNumber() {
        return (((Math.random() * (9999 - 1000))+1000))
                + "-" + ((int)((Math.random() * (9999 - 1000))+1000))
                + "-" + ((int)((Math.random() * (9999 - 1000))+1000))
                + "-" + ((int)((Math.random() * (9999 - 1000))+1000));
    }
}
