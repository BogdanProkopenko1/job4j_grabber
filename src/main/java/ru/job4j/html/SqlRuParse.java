package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Post;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import javax.print.Doc;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SqlRuParse {

    private Post transform(Map<String, Element> map, String link) {
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        LocalDateTime dateTime = parser.parse(map.get("created_date").text());
        return new Post(
                dateTime,
                map.get("author").text(),
                //map.get("link").text(),
                link,
                map.get("message").text(),
                Integer.parseInt(map.get("views").text()),
                Integer.parseInt(map.get("answers").text())
        );
    }

    public static void main(String[] args) throws Exception {
        for (int i = 1; i < 6; i++) {
            Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers/" + i).get();
            Elements messages = doc.select(".postslisttopic");
            Elements authors = doc.select("td:nth-child(3)");
            Elements answers = doc.select("td:nth-child(4)");
            Elements views = doc.select("td:nth-child(5)");
            Elements dates = doc.select("td:nth-child(6)");
            for (int j = 0; j < messages.size() && j < dates.size(); j++) {
                Map<String, Element> map = new HashMap<>();
                map.put("created_date", dates.get(j));
                map.put("author", authors.get(j));
               // map.put("link", messages.get(j).child(0).attr("href"));2.3. Загрузка деталей поста. [#285212]
                map.put("message", messages.get(j));
                map.put("answers", answers.get(j));
                map.put("views", views.get(j));
                SqlRuParse sqlRuParse = new SqlRuParse();
                System.out.println(sqlRuParse.transform(map, messages.get(j).child(0).attr("href")));
                //Element href = messages.get(j).child(0);
                /*
                System.out.println(href.attr("href"));
                System.out.println(href.text());
                System.out.println(dates.get(j).text());
                System.out.println(authors.get(j).text());

                 */
            }
        }
    }
}