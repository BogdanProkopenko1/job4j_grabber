package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Parse;
import ru.job4j.grabber.Post;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class SqlRuParse implements Parse {

    private Post transform(Map<String, Element> map) {
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        LocalDateTime dateTime = parser.parse(map.get("created_date").text());
        return new Post(
                dateTime,
                map.get("author").text(),
                map.get("message").child(0).attr("href"),
                map.get("message").text(),
                Integer.parseInt(map.get("views").text()),
                Integer.parseInt(map.get("answers").text())
        );
    }

    @Override
    public List<Post> list(String link) throws IOException {
        List<Post> posts = new ArrayList<>(55);
        Document document = Jsoup.connect(link).get();
        Elements messages = document.select(".postslisttopic");
        Elements authors = document.select("td:nth-child(3)");
        Elements answers = document.select("td:nth-child(4)");
        Elements views = document.select("td:nth-child(5)");
        Elements dates = document.select("td:nth-child(6)");
        for (int j = 0; j < messages.size(); j++) {
            Map<String, Element> map = new HashMap<>();
            map.put("created_date", dates.get(j));
            map.put("author", authors.get(j));
            map.put("message", messages.get(j));
            map.put("answers", answers.get(j));
            map.put("views", views.get(j));
            posts.add(transform(map));
        }
        return posts;
    }

    @Override
    public Post detail(String link) throws IOException {
        Post post = new Post();
        for (int i = 1; i < 6; i++) {
            Document document = Jsoup.connect("https://www.sql.ru/forum/job-offers/" + i).get();
            Elements messages = document.select(".postslisttopic");
            for (int j = 0; j < messages.size(); j++) {
                String l = messages.get(j).child(0).attr("href");
                if (l.equals(link)) {
                    Element date = document.selectFirst("tr:nth-child(" + j + ") > td:nth-child(6)");
                    Element author = document.selectFirst("tr:nth-child(" + j + ") > td:nth-child(3)");
                    Element answer = document.selectFirst("tr:nth-child(" + j + ") > td:nth-child(4)");
                    Element views = document.selectFirst("tr:nth-child(" + j + ") > td:nth-child(5)");
                    Map<String, Element> map = new HashMap<>();
                    map.put("created_date", date);
                    map.put("author", author);
                    map.put("message", messages.get(j));
                    map.put("answers", answer);
                    map.put("views", views);
                    post = transform(map);
                }
            }
        }
        return post;
    }

    public static void main(String[] args) throws Exception {
        SqlRuParse sqlRuParse = new SqlRuParse();
        System.out.println(sqlRuParse.detail(
                "https://www.sql.ru/forum/1333850/ishhem-javascript-front-end-razrabotchika-udalenno-150-300k"
        ));
        List<Post> allPosts = new ArrayList<>(300);
        for (int i = 1; i < 6; i++) {
            allPosts.addAll(sqlRuParse.list("https://www.sql.ru/forum/job-offers/" + i));
        }
        for (Post post : allPosts) {
            System.out.println(post);
        }
    }
}