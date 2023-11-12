package com.kosherjava.zmanim;

import com.kosherjava.zmanim.util.GeoLocation;

import java.time.Month;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.TimeZone;

public class Main {
    public static void main(String[] args) {
        GeoLocation arcticNunavut = new GeoLocation("Fort Conger, NU Canada", 81.7449398, -64.7945858, 127.0, TimeZone.getTimeZone("EST"));

        ComplexZmanimCalendar cal = new ComplexZmanimCalendar(arcticNunavut);
        ZonedDateTime calendar = ZonedDateTime.of(1, Month.OCTOBER.getValue(), 14, 0,0,0,0, arcticNunavut.getTimeZone().toZoneId());
        System.out.println(calendar.toInstant());
        cal.setCalendar(calendar);
        System.out.println(cal.getAlos72Zmanis());
        System.out.println(cal.getSunrise());
    }
}
