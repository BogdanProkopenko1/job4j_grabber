package ru.job4j.grabber.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {

    private Map<String, Integer> getMap() {
        final Map<String, Integer> MAP = new HashMap<>();
        MAP.put("янв", 1);
        MAP.put("фев", 2);
        MAP.put("мар", 3);
        MAP.put("апр", 4);
        MAP.put("май", 5);
        MAP.put("июн", 6);
        MAP.put("июл", 7);
        MAP.put("авг", 8);
        MAP.put("сен", 9);
        MAP.put("окт", 10);
        MAP.put("ноя", 11);
        MAP.put("дек", 12);
        return MAP;
    }

    @Override
    public LocalDateTime parse(String parse) {
        final Map<String, Integer> MONTHS = getMap();
        String[] fullTime = parse.split(", ");
        String[] time = fullTime[1].split(":");
        LocalTime localTime = LocalTime.of(Integer.parseInt(time[0]), Integer.parseInt(time[1]));
        LocalDate localDate = LocalDate.now();
        if (fullTime[0].equals("вчера")) {
            localDate = localDate.minusDays(1);
        }
        String[] date = fullTime[0].split(" ");
        if (date.length == 3) {
            localDate = LocalDate.of(
                    Integer.parseInt("20" + date[2]),
                    MONTHS.get(date[1]),
                    Integer.parseInt(date[0])
            );
        }
        return LocalDateTime.of(localDate, localTime);
    }

    public static void main(String[] args) {
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        System.out.println(parser.parse("сегодня, 14:14"));
        System.out.println(parser.parse("15 янв 17, 23:56"));
    }
}