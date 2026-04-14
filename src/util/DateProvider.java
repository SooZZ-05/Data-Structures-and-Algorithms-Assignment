/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 * 
 * @author SooZZ
 * @param <T> 
 */

import java.time.*;

public final class DateProvider {
    private static Clock CLOCK = Clock.systemDefaultZone();
    private DateProvider() {}

    public static LocalDate today() { return LocalDate.now(CLOCK); }
    public static LocalDateTime nowDateTime() { return LocalDateTime.now(CLOCK); }

    public static void setFixedDate(LocalDate date) {
        ZoneId z = ZoneId.systemDefault();
        CLOCK = Clock.fixed(date.atStartOfDay(z).toInstant(), z);
    }
    public static void resetSystemDate() {
        CLOCK = Clock.systemDefaultZone();
    }
}