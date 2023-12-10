package com.kosherjava.zmanim.hebrewcalendar;

import com.kosherjava.zmanim.ComplexZmanimCalendar;
import com.kosherjava.zmanim.util.AstronomicalCalculator;
import com.kosherjava.zmanim.util.GeoLocation;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RegressionTestFileWriter {
    private static final GeoLocation location = new GeoLocation("Lakewood, NJ", 40.096, -74.222, 29.02, TimeZone.getTimeZone("America/New_York"));
    private static final LocalDate start = LocalDate.of(1, Month.DECEMBER, 20); // 18 tevet 0002 hebrew
    private static final LocalDate end = LocalDate.of(2239, Month.SEPTEMBER, 30); // 1/1/6000 Hebrew

    private static final int numDays = (int) start.until(end, ChronoUnit.DAYS) + 1/*end is exclusive*/;
    private static int lastPrintedPrecentage = -1;
    private static final List<LocalDate> allDays = IntStream.range(0, numDays).parallel().mapToObj(start::plusDays).collect(Collectors.toList());
    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final LocalTime midnight = LocalTime.of(0, 0, 0);

    static class Pair {
        FullZmanim zmanim;
        FullCalendar cal;

        public Pair(FullZmanim zmanim, FullCalendar cal) {
            this.cal = cal;
            this.zmanim = zmanim;
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Start day: " + start + " End day: " + end + "Num days: " + numDays);
        //generates file with all the Jewish dates and times from start to end

//            cal.setUseModernHolidays();
//            cal.setInIsrael();
//            cal.setIsMukafChoma();


        List<Pair> pairs = allDays.parallelStream().map((day) -> {
                    //TODO work in progress:
                    int percentDone = counter.incrementAndGet() * 100 / numDays;
                    if (percentDone != lastPrintedPrecentage && percentDone % 20 == 0/*percent done is multiple of 20*/) {
                        lastPrintedPrecentage = percentDone;
                        System.out.println(percentDone + "%");
                    }
                    JewishCalendar cal = new JewishCalendar(day);
                    ComplexZmanimCalendar zcal = new ComplexZmanimCalendar(location);
                    Calendar calendar = Calendar.getInstance(location.getTimeZone());
                    calendar.set(Calendar.YEAR, day.getYear());
                    calendar.set(Calendar.MONTH, day.getMonthValue() - 1);
                    calendar.set(Calendar.DAY_OF_MONTH, day.getDayOfMonth());
                    calendar.set(Calendar.HOUR_OF_DAY, midnight.getHour());
                    calendar.set(Calendar.MINUTE, midnight.getMinute());
                    calendar.set(Calendar.SECOND, midnight.getSecond());
                    zcal.setCalendar(calendar);

                    Daf dafYomiBavli = null;
                    Daf dafYomiYerushalmi = null;

                    try {
                        dafYomiBavli = cal.getDafYomiBavli();
                        dafYomiYerushalmi = cal.getDafYomiYerushalmi();
                    } catch (Throwable ignored) {
                    }
                    //deprecated
            /*cal.isVeseinTalUmatarStartDate();
            cal.isVeseinTalUmatarStartingTonight();
            cal.isVeseinTalUmatarRecited();
            cal.isVeseinBerachaRecited();
            cal.isMashivHaruachStartDate();
            cal.isMashivHaruachEndDate();
            cal.isMashivHaruachRecited();
            cal.isMoridHatalRecited();*/
                    return new Pair(
                            new FullZmanim(zcal.getShaahZmanis19Point8Degrees(), zcal.getShaahZmanis18Degrees(), zcal.getShaahZmanis26Degrees(), zcal.getShaahZmanis16Point1Degrees(), zcal.getShaahZmanis60Minutes(), zcal.getShaahZmanis72Minutes(), zcal.getShaahZmanis72MinutesZmanis(), zcal.getShaahZmanis90Minutes(), zcal.getShaahZmanis90MinutesZmanis(), zcal.getShaahZmanis96MinutesZmanis(), zcal.getShaahZmanisAteretTorah(), zcal.getShaahZmanisAlos16Point1ToTzais3Point8(), zcal.getShaahZmanisAlos16Point1ToTzais3Point7(), zcal.getShaahZmanis96Minutes(), zcal.getShaahZmanis120Minutes(), zcal.getShaahZmanis120MinutesZmanis(), zcal.getPlagHamincha120MinutesZmanis(), zcal.getPlagHamincha120Minutes(), zcal.getAlos60(), zcal.getAlos72Zmanis(), zcal.getAlos96(), zcal.getAlos90Zmanis(), zcal.getAlos96Zmanis(), zcal.getAlos90(), zcal.getAlos120(), zcal.getAlos120Zmanis(), zcal.getAlos26Degrees(), zcal.getAlos18Degrees(), zcal.getAlos19Degrees(), zcal.getAlos19Point8Degrees(), zcal.getAlos16Point1Degrees(), zcal.getMisheyakir11Point5Degrees(), zcal.getMisheyakir11Degrees(), zcal.getMisheyakir10Point2Degrees(), zcal.getMisheyakir7Point65Degrees(), zcal.getMisheyakir9Point5Degrees(), zcal.getSunrise(), zcal.getSeaLevelSunrise(), zcal.getSofZmanShmaMGA16Point1Degrees(), zcal.getSofZmanShmaMGA72Minutes(), zcal.getSofZmanShmaMGA72MinutesZmanis(), zcal.getSofZmanShmaMGA90Minutes(), zcal.getSofZmanShmaMGA90MinutesZmanis(), zcal.getSofZmanShmaMGA96Minutes(), zcal.getSofZmanShmaMGA96MinutesZmanis(), zcal.getSofZmanShma3HoursBeforeChatzos(), zcal.getSofZmanShmaMGA120Minutes(), zcal.getSofZmanShmaAlos16Point1ToSunset(), zcal.getSofZmanShmaAlos16Point1ToTzaisGeonim7Point083Degrees(), zcal.getSofZmanShmaKolEliyahu(), zcal.getSofZmanTfilaMGA19Point8Degrees(), zcal.getSofZmanTfilaMGA16Point1Degrees(), zcal.getSofZmanTfilaMGA18Degrees(), zcal.getSofZmanTfilaMGA72Minutes(), zcal.getSofZmanTfilaMGA72MinutesZmanis(), zcal.getSofZmanTfilaMGA90Minutes(), zcal.getSofZmanTfilaMGA90MinutesZmanis(), zcal.getSofZmanTfilaMGA96Minutes(), zcal.getSofZmanTfilaMGA96MinutesZmanis(), zcal.getSofZmanTfilaMGA120Minutes(), zcal.getSofZmanTfila2HoursBeforeChatzos(), zcal.getMinchaGedola30Minutes(), zcal.getMinchaGedola72Minutes(), zcal.getMinchaGedola16Point1Degrees(), zcal.getMinchaGedolaAhavatShalom(), zcal.getMinchaGedolaGreaterThan30(), zcal.getMinchaKetana16Point1Degrees(), zcal.getMinchaKetanaAhavatShalom(), zcal.getMinchaKetana72Minutes(), zcal.getPlagHamincha60Minutes(), zcal.getPlagHamincha72Minutes(), zcal.getPlagHamincha90Minutes(), zcal.getPlagHamincha96Minutes(), zcal.getPlagHamincha96MinutesZmanis(), zcal.getPlagHamincha90MinutesZmanis(), zcal.getPlagHamincha72MinutesZmanis(), zcal.getPlagHamincha16Point1Degrees(), zcal.getPlagHamincha19Point8Degrees(), zcal.getPlagHamincha26Degrees(), zcal.getPlagHamincha18Degrees(), zcal.getPlagAlosToSunset(), zcal.getPlagAlos16Point1ToTzaisGeonim7Point083Degrees(), zcal.getPlagAhavatShalom(), zcal.getBainHashmashosRT13Point24Degrees(), zcal.getBainHasmashosRT13Point24Degrees(), zcal.getBainHashmashosRT58Point5Minutes(), zcal.getBainHasmashosRT58Point5Minutes(), zcal.getBainHashmashosRT13Point5MinutesBefore7Point083Degrees(), zcal.getBainHasmashosRT13Point5MinutesBefore7Point083Degrees(), zcal.getBainHashmashosRT2Stars(), zcal.getBainHasmashosRT2Stars(), zcal.getBainHashmashosYereim18Minutes(), zcal.getBainHasmashosYereim18Minutes(), zcal.getBainHashmashosYereim3Point05Degrees(), zcal.getBainHasmashosYereim3Point05Degrees(), zcal.getBainHashmashosYereim16Point875Minutes(), zcal.getBainHasmashosYereim16Point875Minutes(), zcal.getBainHashmashosYereim2Point8Degrees(), zcal.getBainHasmashosYereim2Point8Degrees(), zcal.getBainHashmashosYereim13Point5Minutes(), zcal.getBainHasmashosYereim13Point5Minutes(), zcal.getBainHashmashosYereim2Point1Degrees(), zcal.getBainHasmashosYereim2Point1Degrees(), zcal.getTzaisGeonim3Point7Degrees(), zcal.getTzaisGeonim3Point8Degrees(), zcal.getTzaisGeonim5Point95Degrees(), zcal.getTzaisGeonim3Point65Degrees(), zcal.getTzaisGeonim3Point676Degrees(), zcal.getTzaisGeonim4Point61Degrees(), zcal.getTzaisGeonim4Point37Degrees(), zcal.getTzaisGeonim5Point88Degrees(), zcal.getTzaisGeonim4Point8Degrees(), zcal.getTzaisGeonim6Point45Degrees(), zcal.getTzaisGeonim7Point083Degrees(), zcal.getTzaisGeonim7Point67Degrees(), zcal.getTzaisGeonim8Point5Degrees(), zcal.getTzaisGeonim9Point3Degrees(), zcal.getTzaisGeonim9Point75Degrees(), zcal.getTzais60(), zcal.getTzaisAteretTorah(), zcal.getSofZmanShmaAteretTorah(), zcal.getSofZmanTfilahAteretTorah(), zcal.getMinchaGedolaAteretTorah(), zcal.getMinchaKetanaAteretTorah(), zcal.getPlagHaminchaAteretTorah(), zcal.getTzais72Zmanis(), zcal.getTzais90Zmanis(), zcal.getTzais96Zmanis(), zcal.getTzais90(), zcal.getTzais120(), zcal.getTzais120Zmanis(), zcal.getTzais16Point1Degrees(), zcal.getTzais26Degrees(), zcal.getTzais18Degrees(), zcal.getTzais19Point8Degrees(), zcal.getTzais96(), zcal.getFixedLocalChatzos(), zcal.getSofZmanShmaFixedLocal(), zcal.getSofZmanTfilaFixedLocal(), zcal.getSofZmanKidushLevanaBetweenMoldos(), zcal.getSofZmanKidushLevana15Days(), zcal.getTchilasZmanKidushLevana3Days(), zcal.getZmanMolad(), zcal.getTchilasZmanKidushLevana7Days(), zcal.getSofZmanAchilasChametzGRA(), zcal.getSofZmanAchilasChametzMGA72Minutes(), zcal.getSofZmanAchilasChametzMGA16Point1Degrees(), zcal.getSofZmanBiurChametzGRA(), zcal.getSofZmanBiurChametzMGA72Minutes(), zcal.getSofZmanBiurChametzMGA16Point1Degrees(), zcal.getSolarMidnight(), zcal.getShaahZmanisBaalHatanya(), zcal.getAlosBaalHatanya(), zcal.getSofZmanShmaBaalHatanya(), zcal.getSofZmanTfilaBaalHatanya(), zcal.getSofZmanAchilasChametzBaalHatanya(), zcal.getSofZmanBiurChametzBaalHatanya(), zcal.getMinchaGedolaBaalHatanya(), zcal.getMinchaGedolaBaalHatanyaGreaterThan30(), zcal.getMinchaKetanaBaalHatanya(), zcal.getPlagHaminchaBaalHatanya(), zcal.getTzaisBaalHatanya(), zcal.getSofZmanShmaMGA18DegreesToFixedLocalChatzos(), zcal.getSofZmanShmaMGA16Point1DegreesToFixedLocalChatzos(), zcal.getSofZmanShmaMGA90MinutesToFixedLocalChatzos(), zcal.getSofZmanShmaMGA72MinutesToFixedLocalChatzos(), zcal.getSofZmanShmaGRASunriseToFixedLocalChatzos(), zcal.getSofZmanTfilaGRASunriseToFixedLocalChatzos(), zcal.getMinchaGedolaGRAFixedLocalChatzos30Minutes(), zcal.getMinchaKetanaGRAFixedLocalChatzosToSunset(), zcal.getPlagHaminchaGRAFixedLocalChatzosToSunset(), zcal.getTzais50(), zcal.getSamuchLeMinchaKetanaGRA(), zcal.getSamuchLeMinchaKetana16Point1Degrees(), zcal.getSamuchLeMinchaKetana72Minutes()),
                            new FullCalendar(day, new JewishDate(day), cal.getYomTovIndex(), dafYomiBavli, dafYomiYerushalmi, cal.isIsruChag(), cal.isBirkasHachamah(), cal.getParshah(), cal.getUpcomingParshah(), cal.getSpecialShabbos(), cal.isYomTov(), cal.isYomTovAssurBemelacha(), cal.isAssurBemelacha(), cal.hasCandleLighting(), cal.isTomorrowShabbosOrYomTov(), cal.isErevYomTovSheni(), cal.isAseresYemeiTeshuva(), cal.isPesach(), cal.isCholHamoedPesach(), cal.isShavuos(), cal.isRoshHashana(), cal.isYomKippur(), cal.isSuccos(), cal.isHoshanaRabba(), cal.isShminiAtzeres(), cal.isSimchasTorah(), cal.isCholHamoedSuccos(), cal.isCholHamoed(), cal.isErevYomTov(), cal.isErevRoshChodesh(), cal.isYomKippurKatan(), cal.isBeHaB(), cal.isTaanis(), cal.isTaanisBechoros(), cal.getDayOfChanukah(), cal.isChanukah(), cal.isPurim(), cal.isRoshChodesh(), cal.isMacharChodesh(), cal.isShabbosMevorchim(), cal.getDayOfOmer(), cal.isTishaBav(), cal.getMolad(), cal.getMoladAsDate(), cal.getTchilasZmanKidushLevana3Days(), cal.getTchilasZmanKidushLevana7Days(), cal.getSofZmanKidushLevanaBetweenMoldos(), cal.getSofZmanKidushLevana15Days(), cal.getTekufasTishreiElapsedDays())
                    );
                })
                .sorted(Comparator.comparing(pair -> pair.cal.current))
                .collect(Collectors.toList());
        //write calendars to file:

        File calendarOutput = new File("lakewood_calendar.csv");
        File zmanimOutput = new File("lakewood_zmanim.csv");
        BufferedWriter calendarWriter = new BufferedWriter(new FileWriter(calendarOutput));
        BufferedWriter zmanimWriter = new BufferedWriter(new FileWriter(zmanimOutput));

        calendarWriter.write(FullCalendar.fields);
        zmanimWriter.write(FullZmanim.fields);

        calendarWriter.newLine();
        zmanimWriter.newLine();

        int chunks = 3;
        int chunkSize = pairs.size() / chunks;

        // batch write in chunks of size chunkSize, which is a 3rd of the results
        for (int i = 0; i < pairs.size(); i += chunkSize) {
            List<Pair> batch = pairs.subList(i, Math.min(i + chunkSize, pairs.size()));
            for (Pair pair : batch) {
                zmanimWriter.append(pair.zmanim.toString());
                zmanimWriter.newLine();

                calendarWriter.append(pair.cal.toString());
                calendarWriter.newLine();
            }
            calendarWriter.flush();
            zmanimWriter.flush();
            System.gc();
        }
        calendarWriter.close();
        zmanimWriter.close();
    }

    static class FullZmanim {
        public static final String fields = "getShaahZmanis19Point8Degrees,getShaahZmanis18Degrees,getShaahZmanis26Degrees,getShaahZmanis16Point1Degrees,getShaahZmanis60Minutes,getShaahZmanis72Minutes,getShaahZmanis72MinutesZmanis,getShaahZmanis90Minutes,getShaahZmanis90MinutesZmanis,getShaahZmanis96MinutesZmanis,getShaahZmanisAteretTorah,getShaahZmanisAlos16Point1ToTzais3Point8,getShaahZmanisAlos16Point1ToTzais3Point7,getShaahZmanis96Minutes,getShaahZmanis120Minutes,getShaahZmanis120MinutesZmanis,getPlagHamincha120MinutesZmanis,getPlagHamincha120Minutes,getAlos60,getAlos72Zmanis,getAlos96,getAlos90Zmanis,getAlos96Zmanis,getAlos90,getAlos120,getAlos120Zmanis,getAlos26Degrees,getAlos18Degrees,getAlos19Degrees,getAlos19Point8Degrees,getAlos16Point1Degrees,getMisheyakir11Point5Degrees,getMisheyakir11Degrees,getMisheyakir10Point2Degrees,getMisheyakir7Point65Degrees,getMisheyakir9Point5Degrees,getSofZmanShmaMGA19Point8Degrees,getSofZmanShmaMGA16Point1Degrees,getSofZmanShmaMGA18Degrees,getSofZmanShmaMGA72Minutes,getSofZmanShmaMGA72MinutesZmanis,getSofZmanShmaMGA90Minutes,getSofZmanShmaMGA90MinutesZmanis,getSofZmanShmaMGA96Minutes,getSofZmanShmaMGA96MinutesZmanis,getSofZmanShma3HoursBeforeChatzos,getSofZmanShmaMGA120Minutes,getSofZmanShmaAlos16Point1ToSunset,getSofZmanShmaAlos16Point1ToTzaisGeonim7Point083Degrees,getSofZmanShmaKolEliyahu,getSofZmanTfilaMGA19Point8Degrees,getSofZmanTfilaMGA16Point1Degrees,getSofZmanTfilaMGA18Degrees,getSofZmanTfilaMGA72Minutes,getSofZmanTfilaMGA72MinutesZmanis,getSofZmanTfilaMGA90Minutes,getSofZmanTfilaMGA90MinutesZmanis,getSofZmanTfilaMGA96Minutes,getSofZmanTfilaMGA96MinutesZmanis,getSofZmanTfilaMGA120Minutes,getSofZmanTfila2HoursBeforeChatzos,getMinchaGedola30Minutes,getMinchaGedola72Minutes,getMinchaGedola16Point1Degrees,getMinchaGedolaAhavatShalom,getMinchaGedolaGreaterThan30,getMinchaKetana16Point1Degrees,getMinchaKetanaAhavatShalom,getMinchaKetana72Minutes,getPlagHamincha60Minutes,getPlagHamincha72Minutes,getPlagHamincha90Minutes,getPlagHamincha96Minutes,getPlagHamincha96MinutesZmanis,getPlagHamincha90MinutesZmanis,getPlagHamincha72MinutesZmanis,getPlagHamincha16Point1Degrees,getPlagHamincha19Point8Degrees,getPlagHamincha26Degrees,getPlagHamincha18Degrees,getPlagAlosToSunset,getPlagAlos16Point1ToTzaisGeonim7Point083Degrees,getPlagAhavatShalom,getBainHashmashosRT13Point24Degrees,getBainHasmashosRT13Point24Degrees,getBainHashmashosRT58Point5Minutes,getBainHasmashosRT58Point5Minutes,getBainHashmashosRT13Point5MinutesBefore7Point083Degrees,getBainHasmashosRT13Point5MinutesBefore7Point083Degrees,getBainHashmashosRT2Stars,getBainHasmashosRT2Stars,getBainHashmashosYereim18Minutes,getBainHasmashosYereim18Minutes,getBainHashmashosYereim3Point05Degrees,getBainHasmashosYereim3Point05Degrees,getBainHashmashosYereim16Point875Minutes,getBainHasmashosYereim16Point875Minutes,getBainHashmashosYereim2Point8Degrees,getBainHasmashosYereim2Point8Degrees,getBainHashmashosYereim13Point5Minutes,getBainHasmashosYereim13Point5Minutes,getBainHashmashosYereim2Point1Degrees,getBainHasmashosYereim2Point1Degrees,getTzaisGeonim3Point7Degrees,getTzaisGeonim3Point8Degrees,getTzaisGeonim5Point95Degrees,getTzaisGeonim3Point65Degrees,getTzaisGeonim3Point676Degrees,getTzaisGeonim4Point61Degrees,getTzaisGeonim4Point37Degrees,getTzaisGeonim5Point88Degrees,getTzaisGeonim4Point8Degrees,getTzaisGeonim6Point45Degrees,getTzaisGeonim7Point083Degrees,getTzaisGeonim7Point67Degrees,getTzaisGeonim8Point5Degrees,getTzaisGeonim9Point3Degrees,getTzaisGeonim9Point75Degrees,getTzais60,getTzaisAteretTorah,getSofZmanShmaAteretTorah,getSofZmanTfilahAteretTorah,getMinchaGedolaAteretTorah,getMinchaKetanaAteretTorah,getPlagHaminchaAteretTorah,getTzais72Zmanis,getTzais90Zmanis,getTzais96Zmanis,getTzais90,getTzais120,getTzais120Zmanis,getTzais16Point1Degrees,getTzais26Degrees,getTzais18Degrees,getTzais19Point8Degrees,getTzais96,getFixedLocalChatzos,getSofZmanShmaFixedLocal,getSofZmanTfilaFixedLocal,getSofZmanKidushLevanaBetweenMoldos,getSofZmanKidushLevana15Days,getTchilasZmanKidushLevana3Days,getZmanMolad,getTchilasZmanKidushLevana7Days,getSofZmanAchilasChametzGRA,getSofZmanAchilasChametzMGA72Minutes,getSofZmanAchilasChametzMGA16Point1Degrees,getSofZmanBiurChametzGRA,getSofZmanBiurChametzMGA72Minutes,getSofZmanBiurChametzMGA16Point1Degrees,getSolarMidnight,getShaahZmanisBaalHatanya,getAlosBaalHatanya,getSofZmanShmaBaalHatanya,getSofZmanTfilaBaalHatanya,getSofZmanAchilasChametzBaalHatanya,getSofZmanBiurChametzBaalHatanya,getMinchaGedolaBaalHatanya,getMinchaGedolaBaalHatanyaGreaterThan30,getMinchaKetanaBaalHatanya,getPlagHaminchaBaalHatanya,getTzaisBaalHatanya,getSofZmanShmaMGA18DegreesToFixedLocalChatzos,getSofZmanShmaMGA16Point1DegreesToFixedLocalChatzos,getSofZmanShmaMGA90MinutesToFixedLocalChatzos,getSofZmanShmaMGA72MinutesToFixedLocalChatzos,getSofZmanShmaGRASunriseToFixedLocalChatzos,getSofZmanTfilaGRASunriseToFixedLocalChatzos,getMinchaGedolaGRAFixedLocalChatzos30Minutes,getMinchaKetanaGRAFixedLocalChatzosToSunset,getPlagHaminchaGRAFixedLocalChatzosToSunset,getTzais50,getSamuchLeMinchaKetanaGRA,getSamuchLeMinchaKetana16Point1Degrees,getSamuchLeMinchaKetana72Minutes";

        @Override
        public String toString() {
            return new StringJoiner(",")
                    .add(Long.toString(getShaahZmanis19Point8Degrees))
                    .add(Long.toString(getShaahZmanis18Degrees))
                    .add(Long.toString(getShaahZmanis26Degrees))
                    .add(Long.toString(getShaahZmanis16Point1Degrees))
                    .add(Long.toString(getShaahZmanis60Minutes))
                    .add(Long.toString(getShaahZmanis72Minutes))
                    .add(Long.toString(getShaahZmanis72MinutesZmanis))
                    .add(Long.toString(getShaahZmanis90Minutes))
                    .add(Long.toString(getShaahZmanis90MinutesZmanis))
                    .add(Long.toString(getShaahZmanis96MinutesZmanis))
                    .add(Long.toString(getShaahZmanisAteretTorah))
                    .add(Long.toString(getShaahZmanisAlos16Point1ToTzais3Point8))
                    .add(Long.toString(getShaahZmanisAlos16Point1ToTzais3Point7))
                    .add(Long.toString(getShaahZmanis96Minutes))
                    .add(Long.toString(getShaahZmanis120Minutes))
                    .add(Long.toString(getShaahZmanis120MinutesZmanis))
                    .add(getPlagHamincha120MinutesZmanis.toInstant().toString())
                    .add(getPlagHamincha120Minutes.toInstant().toString())
                    .add(getAlos60.toInstant().toString())
                    .add(getAlos72Zmanis.toInstant().toString())
                    .add(getAlos96.toInstant().toString())
                    .add(getAlos90Zmanis.toInstant().toString())
                    .add(getAlos96Zmanis.toInstant().toString())
                    .add(getAlos90.toInstant().toString())
                    .add(getAlos120.toInstant().toString())
                    .add(getAlos120Zmanis.toInstant().toString())
                    .add(getAlos26Degrees.toInstant().toString())
                    .add(getAlos18Degrees.toInstant().toString())
                    .add(getAlos19Degrees.toInstant().toString())
                    .add(getAlos19Point8Degrees.toInstant().toString())
                    .add(getAlos16Point1Degrees.toInstant().toString())
                    .add(getMisheyakir11Point5Degrees.toInstant().toString())
                    .add(getMisheyakir11Degrees.toInstant().toString())
                    .add(getMisheyakir10Point2Degrees.toInstant().toString())
                    .add(getMisheyakir7Point65Degrees.toInstant().toString())
                    .add(getMisheyakir9Point5Degrees.toInstant().toString())
                    .add(getSofZmanShmaMGA19Point8Degrees.toInstant().toString())
                    .add(getSofZmanShmaMGA16Point1Degrees.toInstant().toString())
                    .add(getSofZmanShmaMGA18Degrees.toInstant().toString())
                    .add(getSofZmanShmaMGA72Minutes.toInstant().toString())
                    .add(getSofZmanShmaMGA72MinutesZmanis.toInstant().toString())
                    .add(getSofZmanShmaMGA90Minutes.toInstant().toString())
                    .add(getSofZmanShmaMGA90MinutesZmanis.toInstant().toString())
                    .add(getSofZmanShmaMGA96Minutes.toInstant().toString())
                    .add(getSofZmanShmaMGA96MinutesZmanis.toInstant().toString())
                    .add(getSofZmanShma3HoursBeforeChatzos.toInstant().toString())
                    .add(getSofZmanShmaMGA120Minutes.toInstant().toString())
                    .add(getSofZmanShmaAlos16Point1ToSunset.toInstant().toString())
                    .add(getSofZmanShmaAlos16Point1ToTzaisGeonim7Point083Degrees.toInstant().toString())
                    .add(getSofZmanShmaKolEliyahu.toInstant().toString())
                    .add(getSofZmanTfilaMGA19Point8Degrees.toInstant().toString())
                    .add(getSofZmanTfilaMGA16Point1Degrees.toInstant().toString())
                    .add(getSofZmanTfilaMGA18Degrees.toInstant().toString())
                    .add(getSofZmanTfilaMGA72Minutes.toInstant().toString())
                    .add(getSofZmanTfilaMGA72MinutesZmanis.toInstant().toString())
                    .add(getSofZmanTfilaMGA90Minutes.toInstant().toString())
                    .add(getSofZmanTfilaMGA90MinutesZmanis.toInstant().toString())
                    .add(getSofZmanTfilaMGA96Minutes.toInstant().toString())
                    .add(getSofZmanTfilaMGA96MinutesZmanis.toInstant().toString())
                    .add(getSofZmanTfilaMGA120Minutes.toInstant().toString())
                    .add(getSofZmanTfila2HoursBeforeChatzos.toInstant().toString())
                    .add(getMinchaGedola30Minutes.toInstant().toString())
                    .add(getMinchaGedola72Minutes.toInstant().toString())
                    .add(getMinchaGedola16Point1Degrees.toInstant().toString())
                    .add(getMinchaGedolaAhavatShalom.toInstant().toString())
                    .add(getMinchaGedolaGreaterThan30.toInstant().toString())
                    .add(getMinchaKetana16Point1Degrees.toInstant().toString())
                    .add(getMinchaKetanaAhavatShalom.toInstant().toString())
                    .add(getMinchaKetana72Minutes.toInstant().toString())
                    .add(getPlagHamincha60Minutes.toInstant().toString())
                    .add(getPlagHamincha72Minutes.toInstant().toString())
                    .add(getPlagHamincha90Minutes.toInstant().toString())
                    .add(getPlagHamincha96Minutes.toInstant().toString())
                    .add(getPlagHamincha96MinutesZmanis.toInstant().toString())
                    .add(getPlagHamincha90MinutesZmanis.toInstant().toString())
                    .add(getPlagHamincha72MinutesZmanis.toInstant().toString())
                    .add(getPlagHamincha16Point1Degrees.toInstant().toString())
                    .add(getPlagHamincha19Point8Degrees.toInstant().toString())
                    .add(getPlagHamincha26Degrees.toInstant().toString())
                    .add(getPlagHamincha18Degrees.toInstant().toString())
                    .add(getPlagAlosToSunset.toInstant().toString())
                    .add(getPlagAlos16Point1ToTzaisGeonim7Point083Degrees.toInstant().toString())
                    .add(getPlagAhavatShalom.toInstant().toString())
                    .add(getBainHashmashosRT13Point24Degrees.toInstant().toString())
                    .add(getBainHasmashosRT13Point24Degrees.toInstant().toString())
                    .add(getBainHashmashosRT58Point5Minutes.toInstant().toString())
                    .add(getBainHasmashosRT58Point5Minutes.toInstant().toString())
                    .add(getBainHashmashosRT13Point5MinutesBefore7Point083Degrees.toInstant().toString())
                    .add(getBainHasmashosRT13Point5MinutesBefore7Point083Degrees.toInstant().toString())
                    .add(getBainHashmashosRT2Stars.toInstant().toString())
                    .add(getBainHasmashosRT2Stars.toInstant().toString())
                    .add(getBainHashmashosYereim18Minutes.toInstant().toString())
                    .add(getBainHasmashosYereim18Minutes.toInstant().toString())
                    .add(getBainHashmashosYereim3Point05Degrees.toInstant().toString())
                    .add(getBainHasmashosYereim3Point05Degrees.toInstant().toString())
                    .add(getBainHashmashosYereim16Point875Minutes.toInstant().toString())
                    .add(getBainHasmashosYereim16Point875Minutes.toInstant().toString())
                    .add(getBainHashmashosYereim2Point8Degrees.toInstant().toString())
                    .add(getBainHasmashosYereim2Point8Degrees.toInstant().toString())
                    .add(getBainHashmashosYereim13Point5Minutes.toInstant().toString())
                    .add(getBainHasmashosYereim13Point5Minutes.toInstant().toString())
                    .add(getBainHashmashosYereim2Point1Degrees.toInstant().toString())
                    .add(getBainHasmashosYereim2Point1Degrees.toInstant().toString())
                    .add(getTzaisGeonim3Point7Degrees.toInstant().toString())
                    .add(getTzaisGeonim3Point8Degrees.toInstant().toString())
                    .add(getTzaisGeonim5Point95Degrees.toInstant().toString())
                    .add(getTzaisGeonim3Point65Degrees.toInstant().toString())
                    .add(getTzaisGeonim3Point676Degrees.toInstant().toString())
                    .add(getTzaisGeonim4Point61Degrees.toInstant().toString())
                    .add(getTzaisGeonim4Point37Degrees.toInstant().toString())
                    .add(getTzaisGeonim5Point88Degrees.toInstant().toString())
                    .add(getTzaisGeonim4Point8Degrees.toInstant().toString())
                    .add(getTzaisGeonim6Point45Degrees.toInstant().toString())
                    .add(getTzaisGeonim7Point083Degrees.toInstant().toString())
                    .add(getTzaisGeonim7Point67Degrees.toInstant().toString())
                    .add(getTzaisGeonim8Point5Degrees.toInstant().toString())
                    .add(getTzaisGeonim9Point3Degrees.toInstant().toString())
                    .add(getTzaisGeonim9Point75Degrees.toInstant().toString())
                    .add(getTzais60.toInstant().toString())
                    .add(getTzaisAteretTorah.toInstant().toString())
                    .add(getSofZmanShmaAteretTorah.toInstant().toString())
                    .add(getSofZmanTfilahAteretTorah.toInstant().toString())
                    .add(getMinchaGedolaAteretTorah.toInstant().toString())
                    .add(getMinchaKetanaAteretTorah.toInstant().toString())
                    .add(getPlagHaminchaAteretTorah.toInstant().toString())
                    .add(getTzais72Zmanis.toInstant().toString())
                    .add(getTzais90Zmanis.toInstant().toString())
                    .add(getTzais96Zmanis.toInstant().toString())
                    .add(getTzais90.toInstant().toString())
                    .add(getTzais120.toInstant().toString())
                    .add(getTzais120Zmanis.toInstant().toString())
                    .add(getTzais16Point1Degrees.toInstant().toString())
                    .add(getTzais26Degrees.toInstant().toString())
                    .add(getTzais18Degrees.toInstant().toString())
                    .add(getTzais19Point8Degrees.toInstant().toString())
                    .add(getTzais96.toInstant().toString())
                    .add(getFixedLocalChatzos.toInstant().toString())
                    .add(getSofZmanShmaFixedLocal.toInstant().toString())
                    .add(getSofZmanTfilaFixedLocal.toInstant().toString())
                    .add(getSofZmanKidushLevanaBetweenMoldos != null ? getSofZmanKidushLevanaBetweenMoldos.toInstant().toString() : "null")
                    .add(getSofZmanKidushLevana15Days != null ? getSofZmanKidushLevana15Days.toInstant().toString() : "null")
                    .add(getTchilasZmanKidushLevana3Days != null ? getTchilasZmanKidushLevana3Days.toInstant().toString() : "null")
                    .add(getZmanMolad != null ? getZmanMolad.toInstant().toString() : "null")
                    .add(getTchilasZmanKidushLevana7Days != null ? getTchilasZmanKidushLevana7Days.toInstant().toString() : "null")
                    .add(getSofZmanAchilasChametzGRA != null ? getSofZmanAchilasChametzGRA.toInstant().toString() : "null")
                    .add(getSofZmanAchilasChametzMGA72Minutes != null ? getSofZmanAchilasChametzMGA72Minutes.toInstant().toString() : "null")
                    .add(getSofZmanAchilasChametzMGA16Point1Degrees != null ? getSofZmanAchilasChametzMGA16Point1Degrees.toInstant().toString() : "null")
                    .add(getSofZmanBiurChametzGRA != null ? getSofZmanBiurChametzGRA.toInstant().toString() : "null")
                    .add(getSofZmanBiurChametzMGA72Minutes != null ? getSofZmanBiurChametzMGA72Minutes.toInstant().toString() : "null")
                    .add(getSofZmanBiurChametzMGA16Point1Degrees != null ? getSofZmanBiurChametzMGA16Point1Degrees.toInstant().toString() : "null")
                    .add(getSolarMidnight.toInstant().toString())
                    .add(Long.toString(getShaahZmanisBaalHatanya))
                    .add(getAlosBaalHatanya.toInstant().toString())
                    .add(getSofZmanShmaBaalHatanya.toInstant().toString())
                    .add(getSofZmanTfilaBaalHatanya.toInstant().toString())
                    .add(getSofZmanAchilasChametzBaalHatanya.toInstant().toString())
                    .add(getSofZmanBiurChametzBaalHatanya.toInstant().toString())
                    .add(getMinchaGedolaBaalHatanya.toInstant().toString())
                    .add(getMinchaGedolaBaalHatanyaGreaterThan30.toInstant().toString())
                    .add(getMinchaKetanaBaalHatanya.toInstant().toString())
                    .add(getPlagHaminchaBaalHatanya.toInstant().toString())
                    .add(getTzaisBaalHatanya.toInstant().toString())
                    .add(getSofZmanShmaMGA18DegreesToFixedLocalChatzos.toInstant().toString())
                    .add(getSofZmanShmaMGA16Point1DegreesToFixedLocalChatzos.toInstant().toString())
                    .add(getSofZmanShmaMGA90MinutesToFixedLocalChatzos.toInstant().toString())
                    .add(getSofZmanShmaMGA72MinutesToFixedLocalChatzos.toInstant().toString())
                    .add(getSofZmanShmaGRASunriseToFixedLocalChatzos.toInstant().toString())
                    .add(getSofZmanTfilaGRASunriseToFixedLocalChatzos.toInstant().toString())
                    .add(getMinchaGedolaGRAFixedLocalChatzos30Minutes.toInstant().toString())
                    .add(getMinchaKetanaGRAFixedLocalChatzosToSunset.toInstant().toString())
                    .add(getPlagHaminchaGRAFixedLocalChatzosToSunset.toInstant().toString())
                    .add(getTzais50.toInstant().toString())
                    .add(getSamuchLeMinchaKetanaGRA.toInstant().toString())
                    .add(getSamuchLeMinchaKetana16Point1Degrees.toInstant().toString())
                    .add(getSamuchLeMinchaKetana72Minutes.toInstant().toString())
                    .toString();
        }

        public FullZmanim(long getShaahZmanis19Point8Degrees, long getShaahZmanis18Degrees, long getShaahZmanis26Degrees, long getShaahZmanis16Point1Degrees, long getShaahZmanis60Minutes, long getShaahZmanis72Minutes, long getShaahZmanis72MinutesZmanis, long getShaahZmanis90Minutes, long getShaahZmanis90MinutesZmanis, long getShaahZmanis96MinutesZmanis, long getShaahZmanisAteretTorah, long getShaahZmanisAlos16Point1ToTzais3Point8, long getShaahZmanisAlos16Point1ToTzais3Point7, long getShaahZmanis96Minutes, long getShaahZmanis120Minutes, long getShaahZmanis120MinutesZmanis, Date getPlagHamincha120MinutesZmanis, Date getPlagHamincha120Minutes, Date getAlos60, Date getAlos72Zmanis, Date getAlos96, Date getAlos90Zmanis, Date getAlos96Zmanis, Date getAlos90, Date getAlos120, Date getAlos120Zmanis, Date getAlos26Degrees, Date getAlos18Degrees, Date getAlos19Degrees, Date getAlos19Point8Degrees, Date getAlos16Point1Degrees, Date getMisheyakir11Point5Degrees, Date getMisheyakir11Degrees, Date getMisheyakir10Point2Degrees, Date getMisheyakir7Point65Degrees, Date getMisheyakir9Point5Degrees, Date getSofZmanShmaMGA19Point8Degrees, Date getSofZmanShmaMGA16Point1Degrees, Date getSofZmanShmaMGA18Degrees, Date getSofZmanShmaMGA72Minutes, Date getSofZmanShmaMGA72MinutesZmanis, Date getSofZmanShmaMGA90Minutes, Date getSofZmanShmaMGA90MinutesZmanis, Date getSofZmanShmaMGA96Minutes, Date getSofZmanShmaMGA96MinutesZmanis, Date getSofZmanShma3HoursBeforeChatzos, Date getSofZmanShmaMGA120Minutes, Date getSofZmanShmaAlos16Point1ToSunset, Date getSofZmanShmaAlos16Point1ToTzaisGeonim7Point083Degrees, Date getSofZmanShmaKolEliyahu, Date getSofZmanTfilaMGA19Point8Degrees, Date getSofZmanTfilaMGA16Point1Degrees, Date getSofZmanTfilaMGA18Degrees, Date getSofZmanTfilaMGA72Minutes, Date getSofZmanTfilaMGA72MinutesZmanis, Date getSofZmanTfilaMGA90Minutes, Date getSofZmanTfilaMGA90MinutesZmanis, Date getSofZmanTfilaMGA96Minutes, Date getSofZmanTfilaMGA96MinutesZmanis, Date getSofZmanTfilaMGA120Minutes, Date getSofZmanTfila2HoursBeforeChatzos, Date getMinchaGedola30Minutes, Date getMinchaGedola72Minutes, Date getMinchaGedola16Point1Degrees, Date getMinchaGedolaAhavatShalom, Date getMinchaGedolaGreaterThan30, Date getMinchaKetana16Point1Degrees, Date getMinchaKetanaAhavatShalom, Date getMinchaKetana72Minutes, Date getPlagHamincha60Minutes, Date getPlagHamincha72Minutes, Date getPlagHamincha90Minutes, Date getPlagHamincha96Minutes, Date getPlagHamincha96MinutesZmanis, Date getPlagHamincha90MinutesZmanis, Date getPlagHamincha72MinutesZmanis, Date getPlagHamincha16Point1Degrees, Date getPlagHamincha19Point8Degrees, Date getPlagHamincha26Degrees, Date getPlagHamincha18Degrees, Date getPlagAlosToSunset, Date getPlagAlos16Point1ToTzaisGeonim7Point083Degrees, Date getPlagAhavatShalom, Date getBainHashmashosRT13Point24Degrees, Date getBainHasmashosRT13Point24Degrees, Date getBainHashmashosRT58Point5Minutes, Date getBainHasmashosRT58Point5Minutes, Date getBainHashmashosRT13Point5MinutesBefore7Point083Degrees, Date getBainHasmashosRT13Point5MinutesBefore7Point083Degrees, Date getBainHashmashosRT2Stars, Date getBainHasmashosRT2Stars, Date getBainHashmashosYereim18Minutes, Date getBainHasmashosYereim18Minutes, Date getBainHashmashosYereim3Point05Degrees, Date getBainHasmashosYereim3Point05Degrees, Date getBainHashmashosYereim16Point875Minutes, Date getBainHasmashosYereim16Point875Minutes, Date getBainHashmashosYereim2Point8Degrees, Date getBainHasmashosYereim2Point8Degrees, Date getBainHashmashosYereim13Point5Minutes, Date getBainHasmashosYereim13Point5Minutes, Date getBainHashmashosYereim2Point1Degrees, Date getBainHasmashosYereim2Point1Degrees, Date getTzaisGeonim3Point7Degrees, Date getTzaisGeonim3Point8Degrees, Date getTzaisGeonim5Point95Degrees, Date getTzaisGeonim3Point65Degrees, Date getTzaisGeonim3Point676Degrees, Date getTzaisGeonim4Point61Degrees, Date getTzaisGeonim4Point37Degrees, Date getTzaisGeonim5Point88Degrees, Date getTzaisGeonim4Point8Degrees, Date getTzaisGeonim6Point45Degrees, Date getTzaisGeonim7Point083Degrees, Date getTzaisGeonim7Point67Degrees, Date getTzaisGeonim8Point5Degrees, Date getTzaisGeonim9Point3Degrees, Date getTzaisGeonim9Point75Degrees, Date getTzais60, Date getTzaisAteretTorah, Date getSofZmanShmaAteretTorah, Date getSofZmanTfilahAteretTorah, Date getMinchaGedolaAteretTorah, Date getMinchaKetanaAteretTorah, Date getPlagHaminchaAteretTorah, Date getTzais72Zmanis, Date getTzais90Zmanis, Date getTzais96Zmanis, Date getTzais90, Date getTzais120, Date getTzais120Zmanis, Date getTzais16Point1Degrees, Date getTzais26Degrees, Date getTzais18Degrees, Date getTzais19Point8Degrees, Date getTzais96, Date getFixedLocalChatzos, Date getSofZmanShmaFixedLocal, Date getSofZmanTfilaFixedLocal, Date getSofZmanKidushLevanaBetweenMoldos, Date getSofZmanKidushLevana15Days, Date getTchilasZmanKidushLevana3Days, Date getZmanMolad, Date getTchilasZmanKidushLevana7Days, Date getSofZmanAchilasChametzGRA, Date getSofZmanAchilasChametzMGA72Minutes, Date getSofZmanAchilasChametzMGA16Point1Degrees, Date getSofZmanBiurChametzGRA, Date getSofZmanBiurChametzMGA72Minutes, Date getSofZmanBiurChametzMGA16Point1Degrees, Date getSolarMidnight, long getShaahZmanisBaalHatanya, Date getAlosBaalHatanya, Date getSofZmanShmaBaalHatanya, Date getSofZmanTfilaBaalHatanya, Date getSofZmanAchilasChametzBaalHatanya, Date getSofZmanBiurChametzBaalHatanya, Date getMinchaGedolaBaalHatanya, Date getMinchaGedolaBaalHatanyaGreaterThan30, Date getMinchaKetanaBaalHatanya, Date getPlagHaminchaBaalHatanya, Date getTzaisBaalHatanya, Date getSofZmanShmaMGA18DegreesToFixedLocalChatzos, Date getSofZmanShmaMGA16Point1DegreesToFixedLocalChatzos, Date getSofZmanShmaMGA90MinutesToFixedLocalChatzos, Date getSofZmanShmaMGA72MinutesToFixedLocalChatzos, Date getSofZmanShmaGRASunriseToFixedLocalChatzos, Date getSofZmanTfilaGRASunriseToFixedLocalChatzos, Date getMinchaGedolaGRAFixedLocalChatzos30Minutes, Date getMinchaKetanaGRAFixedLocalChatzosToSunset, Date getPlagHaminchaGRAFixedLocalChatzosToSunset, Date getTzais50, Date getSamuchLeMinchaKetanaGRA, Date getSamuchLeMinchaKetana16Point1Degrees, Date getSamuchLeMinchaKetana72Minutes) {
            this.getShaahZmanis19Point8Degrees = getShaahZmanis19Point8Degrees;
            this.getShaahZmanis18Degrees = getShaahZmanis18Degrees;
            this.getShaahZmanis26Degrees = getShaahZmanis26Degrees;
            this.getShaahZmanis16Point1Degrees = getShaahZmanis16Point1Degrees;
            this.getShaahZmanis60Minutes = getShaahZmanis60Minutes;
            this.getShaahZmanis72Minutes = getShaahZmanis72Minutes;
            this.getShaahZmanis72MinutesZmanis = getShaahZmanis72MinutesZmanis;
            this.getShaahZmanis90Minutes = getShaahZmanis90Minutes;
            this.getShaahZmanis90MinutesZmanis = getShaahZmanis90MinutesZmanis;
            this.getShaahZmanis96MinutesZmanis = getShaahZmanis96MinutesZmanis;
            this.getShaahZmanisAteretTorah = getShaahZmanisAteretTorah;
            this.getShaahZmanisAlos16Point1ToTzais3Point8 = getShaahZmanisAlos16Point1ToTzais3Point8;
            this.getShaahZmanisAlos16Point1ToTzais3Point7 = getShaahZmanisAlos16Point1ToTzais3Point7;
            this.getShaahZmanis96Minutes = getShaahZmanis96Minutes;
            this.getShaahZmanis120Minutes = getShaahZmanis120Minutes;
            this.getShaahZmanis120MinutesZmanis = getShaahZmanis120MinutesZmanis;
            this.getPlagHamincha120MinutesZmanis = getPlagHamincha120MinutesZmanis;
            this.getPlagHamincha120Minutes = getPlagHamincha120Minutes;
            this.getAlos60 = getAlos60;
            this.getAlos72Zmanis = getAlos72Zmanis;
            this.getAlos96 = getAlos96;
            this.getAlos90Zmanis = getAlos90Zmanis;
            this.getAlos96Zmanis = getAlos96Zmanis;
            this.getAlos90 = getAlos90;
            this.getAlos120 = getAlos120;
            this.getAlos120Zmanis = getAlos120Zmanis;
            this.getAlos26Degrees = getAlos26Degrees;
            this.getAlos18Degrees = getAlos18Degrees;
            this.getAlos19Degrees = getAlos19Degrees;
            this.getAlos19Point8Degrees = getAlos19Point8Degrees;
            this.getAlos16Point1Degrees = getAlos16Point1Degrees;
            this.getMisheyakir11Point5Degrees = getMisheyakir11Point5Degrees;
            this.getMisheyakir11Degrees = getMisheyakir11Degrees;
            this.getMisheyakir10Point2Degrees = getMisheyakir10Point2Degrees;
            this.getMisheyakir7Point65Degrees = getMisheyakir7Point65Degrees;
            this.getMisheyakir9Point5Degrees = getMisheyakir9Point5Degrees;
            this.getSofZmanShmaMGA19Point8Degrees = getSofZmanShmaMGA19Point8Degrees;
            this.getSofZmanShmaMGA16Point1Degrees = getSofZmanShmaMGA16Point1Degrees;
            this.getSofZmanShmaMGA18Degrees = getSofZmanShmaMGA18Degrees;
            this.getSofZmanShmaMGA72Minutes = getSofZmanShmaMGA72Minutes;
            this.getSofZmanShmaMGA72MinutesZmanis = getSofZmanShmaMGA72MinutesZmanis;
            this.getSofZmanShmaMGA90Minutes = getSofZmanShmaMGA90Minutes;
            this.getSofZmanShmaMGA90MinutesZmanis = getSofZmanShmaMGA90MinutesZmanis;
            this.getSofZmanShmaMGA96Minutes = getSofZmanShmaMGA96Minutes;
            this.getSofZmanShmaMGA96MinutesZmanis = getSofZmanShmaMGA96MinutesZmanis;
            this.getSofZmanShma3HoursBeforeChatzos = getSofZmanShma3HoursBeforeChatzos;
            this.getSofZmanShmaMGA120Minutes = getSofZmanShmaMGA120Minutes;
            this.getSofZmanShmaAlos16Point1ToSunset = getSofZmanShmaAlos16Point1ToSunset;
            this.getSofZmanShmaAlos16Point1ToTzaisGeonim7Point083Degrees = getSofZmanShmaAlos16Point1ToTzaisGeonim7Point083Degrees;
            this.getSofZmanShmaKolEliyahu = getSofZmanShmaKolEliyahu;
            this.getSofZmanTfilaMGA19Point8Degrees = getSofZmanTfilaMGA19Point8Degrees;
            this.getSofZmanTfilaMGA16Point1Degrees = getSofZmanTfilaMGA16Point1Degrees;
            this.getSofZmanTfilaMGA18Degrees = getSofZmanTfilaMGA18Degrees;
            this.getSofZmanTfilaMGA72Minutes = getSofZmanTfilaMGA72Minutes;
            this.getSofZmanTfilaMGA72MinutesZmanis = getSofZmanTfilaMGA72MinutesZmanis;
            this.getSofZmanTfilaMGA90Minutes = getSofZmanTfilaMGA90Minutes;
            this.getSofZmanTfilaMGA90MinutesZmanis = getSofZmanTfilaMGA90MinutesZmanis;
            this.getSofZmanTfilaMGA96Minutes = getSofZmanTfilaMGA96Minutes;
            this.getSofZmanTfilaMGA96MinutesZmanis = getSofZmanTfilaMGA96MinutesZmanis;
            this.getSofZmanTfilaMGA120Minutes = getSofZmanTfilaMGA120Minutes;
            this.getSofZmanTfila2HoursBeforeChatzos = getSofZmanTfila2HoursBeforeChatzos;
            this.getMinchaGedola30Minutes = getMinchaGedola30Minutes;
            this.getMinchaGedola72Minutes = getMinchaGedola72Minutes;
            this.getMinchaGedola16Point1Degrees = getMinchaGedola16Point1Degrees;
            this.getMinchaGedolaAhavatShalom = getMinchaGedolaAhavatShalom;
            this.getMinchaGedolaGreaterThan30 = getMinchaGedolaGreaterThan30;
            this.getMinchaKetana16Point1Degrees = getMinchaKetana16Point1Degrees;
            this.getMinchaKetanaAhavatShalom = getMinchaKetanaAhavatShalom;
            this.getMinchaKetana72Minutes = getMinchaKetana72Minutes;
            this.getPlagHamincha60Minutes = getPlagHamincha60Minutes;
            this.getPlagHamincha72Minutes = getPlagHamincha72Minutes;
            this.getPlagHamincha90Minutes = getPlagHamincha90Minutes;
            this.getPlagHamincha96Minutes = getPlagHamincha96Minutes;
            this.getPlagHamincha96MinutesZmanis = getPlagHamincha96MinutesZmanis;
            this.getPlagHamincha90MinutesZmanis = getPlagHamincha90MinutesZmanis;
            this.getPlagHamincha72MinutesZmanis = getPlagHamincha72MinutesZmanis;
            this.getPlagHamincha16Point1Degrees = getPlagHamincha16Point1Degrees;
            this.getPlagHamincha19Point8Degrees = getPlagHamincha19Point8Degrees;
            this.getPlagHamincha26Degrees = getPlagHamincha26Degrees;
            this.getPlagHamincha18Degrees = getPlagHamincha18Degrees;
            this.getPlagAlosToSunset = getPlagAlosToSunset;
            this.getPlagAlos16Point1ToTzaisGeonim7Point083Degrees = getPlagAlos16Point1ToTzaisGeonim7Point083Degrees;
            this.getPlagAhavatShalom = getPlagAhavatShalom;
            this.getBainHashmashosRT13Point24Degrees = getBainHashmashosRT13Point24Degrees;
            this.getBainHasmashosRT13Point24Degrees = getBainHasmashosRT13Point24Degrees;
            this.getBainHashmashosRT58Point5Minutes = getBainHashmashosRT58Point5Minutes;
            this.getBainHasmashosRT58Point5Minutes = getBainHasmashosRT58Point5Minutes;
            this.getBainHashmashosRT13Point5MinutesBefore7Point083Degrees = getBainHashmashosRT13Point5MinutesBefore7Point083Degrees;
            this.getBainHasmashosRT13Point5MinutesBefore7Point083Degrees = getBainHasmashosRT13Point5MinutesBefore7Point083Degrees;
            this.getBainHashmashosRT2Stars = getBainHashmashosRT2Stars;
            this.getBainHasmashosRT2Stars = getBainHasmashosRT2Stars;
            this.getBainHashmashosYereim18Minutes = getBainHashmashosYereim18Minutes;
            this.getBainHasmashosYereim18Minutes = getBainHasmashosYereim18Minutes;
            this.getBainHashmashosYereim3Point05Degrees = getBainHashmashosYereim3Point05Degrees;
            this.getBainHasmashosYereim3Point05Degrees = getBainHasmashosYereim3Point05Degrees;
            this.getBainHashmashosYereim16Point875Minutes = getBainHashmashosYereim16Point875Minutes;
            this.getBainHasmashosYereim16Point875Minutes = getBainHasmashosYereim16Point875Minutes;
            this.getBainHashmashosYereim2Point8Degrees = getBainHashmashosYereim2Point8Degrees;
            this.getBainHasmashosYereim2Point8Degrees = getBainHasmashosYereim2Point8Degrees;
            this.getBainHashmashosYereim13Point5Minutes = getBainHashmashosYereim13Point5Minutes;
            this.getBainHasmashosYereim13Point5Minutes = getBainHasmashosYereim13Point5Minutes;
            this.getBainHashmashosYereim2Point1Degrees = getBainHashmashosYereim2Point1Degrees;
            this.getBainHasmashosYereim2Point1Degrees = getBainHasmashosYereim2Point1Degrees;
            this.getTzaisGeonim3Point7Degrees = getTzaisGeonim3Point7Degrees;
            this.getTzaisGeonim3Point8Degrees = getTzaisGeonim3Point8Degrees;
            this.getTzaisGeonim5Point95Degrees = getTzaisGeonim5Point95Degrees;
            this.getTzaisGeonim3Point65Degrees = getTzaisGeonim3Point65Degrees;
            this.getTzaisGeonim3Point676Degrees = getTzaisGeonim3Point676Degrees;
            this.getTzaisGeonim4Point61Degrees = getTzaisGeonim4Point61Degrees;
            this.getTzaisGeonim4Point37Degrees = getTzaisGeonim4Point37Degrees;
            this.getTzaisGeonim5Point88Degrees = getTzaisGeonim5Point88Degrees;
            this.getTzaisGeonim4Point8Degrees = getTzaisGeonim4Point8Degrees;
            this.getTzaisGeonim6Point45Degrees = getTzaisGeonim6Point45Degrees;
            this.getTzaisGeonim7Point083Degrees = getTzaisGeonim7Point083Degrees;
            this.getTzaisGeonim7Point67Degrees = getTzaisGeonim7Point67Degrees;
            this.getTzaisGeonim8Point5Degrees = getTzaisGeonim8Point5Degrees;
            this.getTzaisGeonim9Point3Degrees = getTzaisGeonim9Point3Degrees;
            this.getTzaisGeonim9Point75Degrees = getTzaisGeonim9Point75Degrees;
            this.getTzais60 = getTzais60;
            this.getTzaisAteretTorah = getTzaisAteretTorah;
            this.getSofZmanShmaAteretTorah = getSofZmanShmaAteretTorah;
            this.getSofZmanTfilahAteretTorah = getSofZmanTfilahAteretTorah;
            this.getMinchaGedolaAteretTorah = getMinchaGedolaAteretTorah;
            this.getMinchaKetanaAteretTorah = getMinchaKetanaAteretTorah;
            this.getPlagHaminchaAteretTorah = getPlagHaminchaAteretTorah;
            this.getTzais72Zmanis = getTzais72Zmanis;

            this.getTzais90Zmanis = getTzais90Zmanis;
            this.getTzais96Zmanis = getTzais96Zmanis;
            this.getTzais90 = getTzais90;
            this.getTzais120 = getTzais120;
            this.getTzais120Zmanis = getTzais120Zmanis;
            this.getTzais16Point1Degrees = getTzais16Point1Degrees;
            this.getTzais26Degrees = getTzais26Degrees;
            this.getTzais18Degrees = getTzais18Degrees;
            this.getTzais19Point8Degrees = getTzais19Point8Degrees;
            this.getTzais96 = getTzais96;
            this.getFixedLocalChatzos = getFixedLocalChatzos;
            this.getSofZmanShmaFixedLocal = getSofZmanShmaFixedLocal;
            this.getSofZmanTfilaFixedLocal = getSofZmanTfilaFixedLocal;
            this.getSofZmanKidushLevanaBetweenMoldos = getSofZmanKidushLevanaBetweenMoldos;
            this.getSofZmanKidushLevana15Days = getSofZmanKidushLevana15Days;
            this.getTchilasZmanKidushLevana3Days = getTchilasZmanKidushLevana3Days;
            this.getZmanMolad = getZmanMolad;
            this.getTchilasZmanKidushLevana7Days = getTchilasZmanKidushLevana7Days;
            this.getSofZmanAchilasChametzGRA = getSofZmanAchilasChametzGRA;
            this.getSofZmanAchilasChametzMGA72Minutes = getSofZmanAchilasChametzMGA72Minutes;
            this.getSofZmanAchilasChametzMGA16Point1Degrees = getSofZmanAchilasChametzMGA16Point1Degrees;
            this.getSofZmanBiurChametzGRA = getSofZmanBiurChametzGRA;
            this.getSofZmanBiurChametzMGA72Minutes = getSofZmanBiurChametzMGA72Minutes;
            this.getSofZmanBiurChametzMGA16Point1Degrees = getSofZmanBiurChametzMGA16Point1Degrees;
            this.getSolarMidnight = getSolarMidnight;
            this.getShaahZmanisBaalHatanya = getShaahZmanisBaalHatanya;
            this.getAlosBaalHatanya = getAlosBaalHatanya;
            this.getSofZmanShmaBaalHatanya = getSofZmanShmaBaalHatanya;
            this.getSofZmanTfilaBaalHatanya = getSofZmanTfilaBaalHatanya;
            this.getSofZmanAchilasChametzBaalHatanya = getSofZmanAchilasChametzBaalHatanya;
            this.getSofZmanBiurChametzBaalHatanya = getSofZmanBiurChametzBaalHatanya;
            this.getMinchaGedolaBaalHatanya = getMinchaGedolaBaalHatanya;
            this.getMinchaGedolaBaalHatanyaGreaterThan30 = getMinchaGedolaBaalHatanyaGreaterThan30;
            this.getMinchaKetanaBaalHatanya = getMinchaKetanaBaalHatanya;
            this.getPlagHaminchaBaalHatanya = getPlagHaminchaBaalHatanya;
            this.getTzaisBaalHatanya = getTzaisBaalHatanya;
            this.getSofZmanShmaMGA18DegreesToFixedLocalChatzos = getSofZmanShmaMGA18DegreesToFixedLocalChatzos;
            this.getSofZmanShmaMGA16Point1DegreesToFixedLocalChatzos = getSofZmanShmaMGA16Point1DegreesToFixedLocalChatzos;
            this.getSofZmanShmaMGA90MinutesToFixedLocalChatzos = getSofZmanShmaMGA90MinutesToFixedLocalChatzos;
            this.getSofZmanShmaMGA72MinutesToFixedLocalChatzos = getSofZmanShmaMGA72MinutesToFixedLocalChatzos;
            this.getSofZmanShmaGRASunriseToFixedLocalChatzos = getSofZmanShmaGRASunriseToFixedLocalChatzos;
            this.getSofZmanTfilaGRASunriseToFixedLocalChatzos = getSofZmanTfilaGRASunriseToFixedLocalChatzos;
            this.getMinchaGedolaGRAFixedLocalChatzos30Minutes = getMinchaGedolaGRAFixedLocalChatzos30Minutes;
            this.getMinchaKetanaGRAFixedLocalChatzosToSunset = getMinchaKetanaGRAFixedLocalChatzosToSunset;
            this.getPlagHaminchaGRAFixedLocalChatzosToSunset = getPlagHaminchaGRAFixedLocalChatzosToSunset;
            this.getTzais50 = getTzais50;
            this.getSamuchLeMinchaKetanaGRA = getSamuchLeMinchaKetanaGRA;
            this.getSamuchLeMinchaKetana16Point1Degrees = getSamuchLeMinchaKetana16Point1Degrees;
            this.getSamuchLeMinchaKetana72Minutes = getSamuchLeMinchaKetana72Minutes;
        }

        public final long getShaahZmanis19Point8Degrees;
        public final long getShaahZmanis18Degrees;
        public final long getShaahZmanis26Degrees;
        public final long getShaahZmanis16Point1Degrees;
        public final long getShaahZmanis60Minutes;
        public final long getShaahZmanis72Minutes;
        public final long getShaahZmanis72MinutesZmanis;
        public final long getShaahZmanis90Minutes;
        public final long getShaahZmanis90MinutesZmanis;
        public final long getShaahZmanis96MinutesZmanis;
        public final long getShaahZmanisAteretTorah;
        public final long getShaahZmanisAlos16Point1ToTzais3Point8;
        public final long getShaahZmanisAlos16Point1ToTzais3Point7;
        public final long getShaahZmanis96Minutes;
        public final long getShaahZmanis120Minutes;
        public final long getShaahZmanis120MinutesZmanis;
        public final Date getPlagHamincha120MinutesZmanis;
        public final Date getPlagHamincha120Minutes;
        public final Date getAlos60;
        public final Date getAlos72Zmanis;
        public final Date getAlos96;
        public final Date getAlos90Zmanis;
        public final Date getAlos96Zmanis;
        public final Date getAlos90;
        public final Date getAlos120;
        public final Date getAlos120Zmanis;
        public final Date getAlos26Degrees;
        public final Date getAlos18Degrees;
        public final Date getAlos19Degrees;
        public final Date getAlos19Point8Degrees;
        public final Date getAlos16Point1Degrees;
        public final Date getMisheyakir11Point5Degrees;
        public final Date getMisheyakir11Degrees;
        public final Date getMisheyakir10Point2Degrees;
        public final Date getMisheyakir7Point65Degrees;
        public final Date getMisheyakir9Point5Degrees;
        public final Date getSofZmanShmaMGA19Point8Degrees;
        public final Date getSofZmanShmaMGA16Point1Degrees;
        public final Date getSofZmanShmaMGA18Degrees;
        public final Date getSofZmanShmaMGA72Minutes;
        public final Date getSofZmanShmaMGA72MinutesZmanis;
        public final Date getSofZmanShmaMGA90Minutes;
        public final Date getSofZmanShmaMGA90MinutesZmanis;
        public final Date getSofZmanShmaMGA96Minutes;
        public final Date getSofZmanShmaMGA96MinutesZmanis;
        public final Date getSofZmanShma3HoursBeforeChatzos;
        public final Date getSofZmanShmaMGA120Minutes;
        public final Date getSofZmanShmaAlos16Point1ToSunset;
        public final Date getSofZmanShmaAlos16Point1ToTzaisGeonim7Point083Degrees;
        public final Date getSofZmanShmaKolEliyahu;
        public final Date getSofZmanTfilaMGA19Point8Degrees;
        public final Date getSofZmanTfilaMGA16Point1Degrees;
        public final Date getSofZmanTfilaMGA18Degrees;
        public final Date getSofZmanTfilaMGA72Minutes;
        public final Date getSofZmanTfilaMGA72MinutesZmanis;
        public final Date getSofZmanTfilaMGA90Minutes;
        public final Date getSofZmanTfilaMGA90MinutesZmanis;
        public final Date getSofZmanTfilaMGA96Minutes;
        public final Date getSofZmanTfilaMGA96MinutesZmanis;
        public final Date getSofZmanTfilaMGA120Minutes;
        public final Date getSofZmanTfila2HoursBeforeChatzos;
        public final Date getMinchaGedola30Minutes;
        public final Date getMinchaGedola72Minutes;
        public final Date getMinchaGedola16Point1Degrees;
        public final Date getMinchaGedolaAhavatShalom;
        public final Date getMinchaGedolaGreaterThan30;
        public final Date getMinchaKetana16Point1Degrees;
        public final Date getMinchaKetanaAhavatShalom;
        public final Date getMinchaKetana72Minutes;
        public final Date getPlagHamincha60Minutes;
        public final Date getPlagHamincha72Minutes;
        public final Date getPlagHamincha90Minutes;
        public final Date getPlagHamincha96Minutes;
        public final Date getPlagHamincha96MinutesZmanis;
        public final Date getPlagHamincha90MinutesZmanis;
        public final Date getPlagHamincha72MinutesZmanis;
        public final Date getPlagHamincha16Point1Degrees;
        public final Date getPlagHamincha19Point8Degrees;
        public final Date getPlagHamincha26Degrees;
        public final Date getPlagHamincha18Degrees;
        public final Date getPlagAlosToSunset;
        public final Date getPlagAlos16Point1ToTzaisGeonim7Point083Degrees;
        public final Date getPlagAhavatShalom;
        public final Date getBainHashmashosRT13Point24Degrees;
        public final Date getBainHasmashosRT13Point24Degrees;
        public final Date getBainHashmashosRT58Point5Minutes;
        public final Date getBainHasmashosRT58Point5Minutes;
        public final Date getBainHashmashosRT13Point5MinutesBefore7Point083Degrees;
        public final Date getBainHasmashosRT13Point5MinutesBefore7Point083Degrees;
        public final Date getBainHashmashosRT2Stars;
        public final Date getBainHasmashosRT2Stars;
        public final Date getBainHashmashosYereim18Minutes;
        public final Date getBainHasmashosYereim18Minutes;
        public final Date getBainHashmashosYereim3Point05Degrees;
        public final Date getBainHasmashosYereim3Point05Degrees;
        public final Date getBainHashmashosYereim16Point875Minutes;
        public final Date getBainHasmashosYereim16Point875Minutes;
        public final Date getBainHashmashosYereim2Point8Degrees;
        public final Date getBainHasmashosYereim2Point8Degrees;
        public final Date getBainHashmashosYereim13Point5Minutes;
        public final Date getBainHasmashosYereim13Point5Minutes;
        public final Date getBainHashmashosYereim2Point1Degrees;
        public final Date getBainHasmashosYereim2Point1Degrees;
        public final Date getTzaisGeonim3Point7Degrees;
        public final Date getTzaisGeonim3Point8Degrees;
        public final Date getTzaisGeonim5Point95Degrees;
        public final Date getTzaisGeonim3Point65Degrees;
        public final Date getTzaisGeonim3Point676Degrees;
        public final Date getTzaisGeonim4Point61Degrees;
        public final Date getTzaisGeonim4Point37Degrees;
        public final Date getTzaisGeonim5Point88Degrees;
        public final Date getTzaisGeonim4Point8Degrees;
        public final Date getTzaisGeonim6Point45Degrees;
        public final Date getTzaisGeonim7Point083Degrees;
        public final Date getTzaisGeonim7Point67Degrees;
        public final Date getTzaisGeonim8Point5Degrees;
        public final Date getTzaisGeonim9Point3Degrees;
        public final Date getTzaisGeonim9Point75Degrees;
        public final Date getTzais60;
        public final Date getTzaisAteretTorah;
        public final Date getSofZmanShmaAteretTorah;
        public final Date getSofZmanTfilahAteretTorah;
        public final Date getMinchaGedolaAteretTorah;
        public final Date getMinchaKetanaAteretTorah;
        public final Date getPlagHaminchaAteretTorah;
        public final Date getTzais72Zmanis;
        public final Date getTzais90Zmanis;
        public final Date getTzais96Zmanis;
        public final Date getTzais90;
        public final Date getTzais120;
        public final Date getTzais120Zmanis;
        public final Date getTzais16Point1Degrees;
        public final Date getTzais26Degrees;
        public final Date getTzais18Degrees;
        public final Date getTzais19Point8Degrees;
        public final Date getTzais96;
        public final Date getFixedLocalChatzos;
        public final Date getSofZmanShmaFixedLocal;
        public final Date getSofZmanTfilaFixedLocal;
        public final Date getSofZmanKidushLevanaBetweenMoldos;
        public final Date getSofZmanKidushLevana15Days;
        public final Date getTchilasZmanKidushLevana3Days;
        public final Date getZmanMolad;
        public final Date getTchilasZmanKidushLevana7Days;
        public final Date getSofZmanAchilasChametzGRA;
        public final Date getSofZmanAchilasChametzMGA72Minutes;
        public final Date getSofZmanAchilasChametzMGA16Point1Degrees;
        public final Date getSofZmanBiurChametzGRA;
        public final Date getSofZmanBiurChametzMGA72Minutes;
        public final Date getSofZmanBiurChametzMGA16Point1Degrees;
        public final Date getSolarMidnight;
        public final long getShaahZmanisBaalHatanya;
        public final Date getAlosBaalHatanya;
        public final Date getSofZmanShmaBaalHatanya;
        public final Date getSofZmanTfilaBaalHatanya;
        public final Date getSofZmanAchilasChametzBaalHatanya;
        public final Date getSofZmanBiurChametzBaalHatanya;
        public final Date getMinchaGedolaBaalHatanya;
        public final Date getMinchaGedolaBaalHatanyaGreaterThan30;
        public final Date getMinchaKetanaBaalHatanya;
        public final Date getPlagHaminchaBaalHatanya;
        public final Date getTzaisBaalHatanya;
        public final Date getSofZmanShmaMGA18DegreesToFixedLocalChatzos;
        public final Date getSofZmanShmaMGA16Point1DegreesToFixedLocalChatzos;
        public final Date getSofZmanShmaMGA90MinutesToFixedLocalChatzos;
        public final Date getSofZmanShmaMGA72MinutesToFixedLocalChatzos;
        public final Date getSofZmanShmaGRASunriseToFixedLocalChatzos;
        public final Date getSofZmanTfilaGRASunriseToFixedLocalChatzos;
        public final Date getMinchaGedolaGRAFixedLocalChatzos30Minutes;
        public final Date getMinchaKetanaGRAFixedLocalChatzosToSunset;
        public final Date getPlagHaminchaGRAFixedLocalChatzosToSunset;
        public final Date getTzais50;
        public final Date getSamuchLeMinchaKetanaGRA;
        public final Date getSamuchLeMinchaKetana16Point1Degrees;
        public final Date getSamuchLeMinchaKetana72Minutes;
    }
    
    /*static class FullAstronomicalCalculator {
        Date getSunrise
        Date getSeaLevelSunrise
        Date getBeginCivilTwilight
        Date getBeginNauticalTwilight
        Date getBeginAstronomicalTwilight
        Date getSunset
        Date getSeaLevelSunset
        Date getEndCivilTwilight
        Date getEndNauticalTwilight
        Date getEndAstronomicalTwilight
        Date getTimeOffset
        Date getTimeOffset
        Date getSunriseOffsetByDegrees
        Date getSunsetOffsetByDegrees
        double getUTCSunrise
        double getUTCSeaLevelSunrise
        double getUTCSunset
        double getUTCSeaLevelSunset
        long getTemporalHour
        long getTemporalHour
        Date getSunTransit
        Date getSunTransit
        Date getDateFromTime
        double getSunriseSolarDipFromOffset
        double getSunsetSolarDipFromOffset
        Calendar getAdjustedCalendar
        GeoLocation getGeoLocation
        AstronomicalCalculator getAstronomicalCalculator
        Calendar getCalendar
    }*/

    static class FullCalendar {
        public static final String fields = "current,currentJewishDate,yomTovIndex,dafYomiBavli,dafYomiYerushalmi,isruChag,birkasHachamah,parshah,upcomingParshah,specialShabbos,yomTov,yomTovAssurBemelacha,assurBemelacha,hasCandleLighting,tomorrowShabbosOrYomTov,erevYomTovSheni,aseresYemeiTeshuva,pesach,cholHamoedPesach,shavuos,roshHashana,yomKippur,succos,hoshanaRabba,shminiAtzeres,simchasTorah,cholHamoedSuccos,cholHamoed,erevYomTov,erevRoshChodesh,yomKippurKatan,beHaB,taanis,taanisBechoros,dayOfChanukah,chanukah,purim,roshChodesh,macharChodesh,shabbosMevorchim,dayOfOmer,tishaBav,molad,moladAsDate,tchilasZmanKidushLevana3Days,tchilasZmanKidushLevana7Days,sofZmanKidushLevanaBetweenMoldos,sofZmanKidushLevana15Days,tekufasTishreiElapsedDays";

        @Override
        public String toString() {
            //escaped for jewish date which contains comma, all escaped for ease of parsing
            return new StringJoiner(",")
                    .add("\"" + current + "\"")
                    .add("\"" + currentJewishDate + "\"")
                    .add("\"" + yomTovIndex + "\"")
                    .add("\"" + (dafYomiBavli == null ? "null" : dafYomiBavli.getMasechtaTransliterated() + " " + dafYomiBavli.getDaf()) + "\"")
                    .add("\"" + (dafYomiYerushalmi == null ? "null" : dafYomiYerushalmi.getMasechtaTransliterated() + " " + dafYomiYerushalmi.getDaf()) + "\"")
                    .add("\"" + isruChag + "\"")
                    .add("\"" + birkasHachamah + "\"")
                    .add("\"" + parshah + "\"")
                    .add("\"" + upcomingParshah + "\"")
                    .add("\"" + specialShabbos + "\"")
                    .add("\"" + yomTov + "\"")
                    .add("\"" + yomTovAssurBemelacha + "\"")
                    .add("\"" + assurBemelacha + "\"")
                    .add("\"" + hasCandleLighting + "\"")
                    .add("\"" + tomorrowShabbosOrYomTov + "\"")
                    .add("\"" + erevYomTovSheni + "\"")
                    .add("\"" + aseresYemeiTeshuva + "\"")
                    .add("\"" + pesach + "\"")
                    .add("\"" + cholHamoedPesach + "\"")
                    .add("\"" + shavuos + "\"")
                    .add("\"" + roshHashana + "\"")
                    .add("\"" + yomKippur + "\"")
                    .add("\"" + succos + "\"")
                    .add("\"" + hoshanaRabba + "\"")
                    .add("\"" + shminiAtzeres + "\"")
                    .add("\"" + simchasTorah + "\"")
                    .add("\"" + cholHamoedSuccos + "\"")
                    .add("\"" + cholHamoed + "\"")
                    .add("\"" + erevYomTov + "\"")
                    .add("\"" + erevRoshChodesh + "\"")
                    .add("\"" + yomKippurKatan + "\"")
                    .add("\"" + beHaB + "\"")
                    .add("\"" + taanis + "\"")
                    .add("\"" + taanisBechoros + "\"")
                    .add("\"" + dayOfChanukah + "\"")
                    .add("\"" + chanukah + "\"")
                    .add("\"" + purim + "\"")
                    .add("\"" + roshChodesh + "\"")
                    .add("\"" + macharChodesh + "\"")
                    .add("\"" + shabbosMevorchim + "\"")
                    .add("\"" + dayOfOmer + "\"")
                    .add("\"" + tishaBav + "\"")
                    .add("\"" + molad + "\"")
                    .add("\"" + moladAsDate + "\"")
                    .add("\"" + tchilasZmanKidushLevana3Days + "\"")
                    .add("\"" + tchilasZmanKidushLevana7Days + "\"")
                    .add("\"" + sofZmanKidushLevanaBetweenMoldos + "\"")
                    .add("\"" + sofZmanKidushLevana15Days + "\"")
                    .add("\"" + tekufasTishreiElapsedDays + "\"")
                    .toString();
        }

        private final LocalDate current;
        private final JewishDate currentJewishDate;
        private final int yomTovIndex;
        private final Daf dafYomiBavli;
        private final Daf dafYomiYerushalmi;
        private final boolean isruChag;
        private final boolean birkasHachamah;
        private final JewishCalendar.Parsha parshah;
        private final JewishCalendar.Parsha upcomingParshah;
        private final JewishCalendar.Parsha specialShabbos;
        private final boolean yomTov;
        private final boolean yomTovAssurBemelacha;
        private final boolean assurBemelacha;
        private final boolean hasCandleLighting;
        private final boolean tomorrowShabbosOrYomTov;
        private final boolean erevYomTovSheni;
        private final boolean aseresYemeiTeshuva;
        private final boolean pesach;
        private final boolean cholHamoedPesach;
        private final boolean shavuos;
        private final boolean roshHashana;
        private final boolean yomKippur;
        private final boolean succos;
        private final boolean hoshanaRabba;
        private final boolean shminiAtzeres;
        private final boolean simchasTorah;
        private final boolean cholHamoedSuccos;
        private final boolean cholHamoed;
        private final boolean erevYomTov;
        private final boolean erevRoshChodesh;
        private final boolean yomKippurKatan;
        private final boolean beHaB;
        private final boolean taanis;
        private final boolean taanisBechoros;
        private final int dayOfChanukah;
        private final boolean chanukah;
        private final boolean purim;
        private final boolean roshChodesh;
        private final boolean macharChodesh;
        private final boolean shabbosMevorchim;
        private final int dayOfOmer;
        private final boolean tishaBav;
        private final JewishDate molad;
        private final Date moladAsDate;
        private final Date tchilasZmanKidushLevana3Days;
        private final Date tchilasZmanKidushLevana7Days;
        private final Date sofZmanKidushLevanaBetweenMoldos;
        private final Date sofZmanKidushLevana15Days;
        private final int tekufasTishreiElapsedDays;

        public FullCalendar(LocalDate current, JewishDate currentJewishDate, int yomTovIndex, Daf dafYomiBavli, Daf dafYomiYerushalmi, boolean isruChag, boolean birkasHachamah, JewishCalendar.Parsha parshah, JewishCalendar.Parsha upcomingParshah, JewishCalendar.Parsha specialShabbos, boolean yomTov, boolean yomTovAssurBemelacha, boolean assurBemelacha, boolean hasCandleLighting, boolean tomorrowShabbosOrYomTov, boolean erevYomTovSheni, boolean aseresYemeiTeshuva, boolean pesach, boolean cholHamoedPesach, boolean shavuos, boolean roshHashana, boolean yomKippur, boolean succos, boolean hoshanaRabba, boolean shminiAtzeres, boolean simchasTorah, boolean cholHamoedSuccos, boolean cholHamoed, boolean erevYomTov, boolean erevRoshChodesh, boolean yomKippurKatan, boolean beHaB, boolean taanis, boolean taanisBechoros, int dayOfChanukah, boolean chanukah, boolean purim, boolean roshChodesh, boolean macharChodesh, boolean shabbosMevorchim, int dayOfOmer, boolean tishaBav, JewishDate molad, Date moladAsDate, Date tchilasZmanKidushLevana3Days, Date tchilasZmanKidushLevana7Days, Date sofZmanKidushLevanaBetweenMoldos, Date sofZmanKidushLevana15Days, int tekufasTishreiElapsedDays) {
            this.current = current;
            this.currentJewishDate = currentJewishDate;
            this.yomTovIndex = yomTovIndex;
            this.dafYomiBavli = dafYomiBavli;
            this.dafYomiYerushalmi = dafYomiYerushalmi;
            this.isruChag = isruChag;
            this.birkasHachamah = birkasHachamah;
            this.parshah = parshah;
            this.upcomingParshah = upcomingParshah;
            this.specialShabbos = specialShabbos;
            this.yomTov = yomTov;
            this.yomTovAssurBemelacha = yomTovAssurBemelacha;
            this.assurBemelacha = assurBemelacha;
            this.hasCandleLighting = hasCandleLighting;
            this.tomorrowShabbosOrYomTov = tomorrowShabbosOrYomTov;
            this.erevYomTovSheni = erevYomTovSheni;
            this.aseresYemeiTeshuva = aseresYemeiTeshuva;
            this.pesach = pesach;
            this.cholHamoedPesach = cholHamoedPesach;
            this.shavuos = shavuos;
            this.roshHashana = roshHashana;
            this.yomKippur = yomKippur;
            this.succos = succos;
            this.hoshanaRabba = hoshanaRabba;
            this.shminiAtzeres = shminiAtzeres;
            this.simchasTorah = simchasTorah;
            this.cholHamoedSuccos = cholHamoedSuccos;
            this.cholHamoed = cholHamoed;
            this.erevYomTov = erevYomTov;
            this.erevRoshChodesh = erevRoshChodesh;
            this.yomKippurKatan = yomKippurKatan;
            this.beHaB = beHaB;
            this.taanis = taanis;
            this.taanisBechoros = taanisBechoros;
            this.dayOfChanukah = dayOfChanukah;
            this.chanukah = chanukah;
            this.purim = purim;
            this.roshChodesh = roshChodesh;
            this.macharChodesh = macharChodesh;
            this.shabbosMevorchim = shabbosMevorchim;
            this.dayOfOmer = dayOfOmer;
            this.tishaBav = tishaBav;
            this.molad = molad;
            this.moladAsDate = moladAsDate;
            this.tchilasZmanKidushLevana3Days = tchilasZmanKidushLevana3Days;
            this.tchilasZmanKidushLevana7Days = tchilasZmanKidushLevana7Days;
            this.sofZmanKidushLevanaBetweenMoldos = sofZmanKidushLevanaBetweenMoldos;
            this.sofZmanKidushLevana15Days = sofZmanKidushLevana15Days;
            this.tekufasTishreiElapsedDays = tekufasTishreiElapsedDays;
        }
    }
}
