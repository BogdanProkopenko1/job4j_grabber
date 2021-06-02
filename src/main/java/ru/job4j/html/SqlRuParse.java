package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SqlRuParse {

    public static void main(String[] args) throws Exception {
        for (int i = 1; i < 6; i++) {
            Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers/" + i).get();
            Elements row = doc.select(".postslisttopic");
            Elements dates = doc.select("td:nth-child(6)");
            for (int j = 0; j < row.size() && j < dates.size(); j++) {
                Element href = row.get(j).child(0);
                System.out.println(href.attr("href"));
                System.out.println(href.text());
                System.out.println(dates.get(j).text());
            }
        }
    }
}