package ru.job4j.grabber.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {

    private Map<String, Integer> getMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("янв", 1);
        map.put("фев", 2);
        map.put("мар", 3);
        map.put("апр", 4);
        map.put("май", 5);
        map.put("июн", 6);
        map.put("июл", 7);
        map.put("авг", 8);
        map.put("сен", 9);
        map.put("окт", 10);
        map.put("ноя", 11);
        map.put("дек", 12);
        return map;
    }

    @Override
    public LocalDateTime parse(String parse) {
        Map<String, Integer> months = getMap();
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
                    months.get(date[1]),
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