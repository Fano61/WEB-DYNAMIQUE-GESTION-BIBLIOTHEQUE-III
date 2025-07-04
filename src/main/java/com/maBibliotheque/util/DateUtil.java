package com.maBibliotheque.util;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DateUtil {
    public static LocalDate ajouterJoursOuvrables(LocalDate date, int jours) {
        int ajoutes = 0;
        while (ajoutes < jours) {
            date = date.plusDays(1);
            if (!(date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                  date.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                ajoutes++;
            }
        }
        return date;
    }
}
