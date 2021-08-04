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

    private static Post transform(Element name, Element text, Element date, String link) {
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        LocalDateTime dateTime = parser.parse(date.text());
        return new Post(
                dateTime,
                name.text(),
                text.text(),
                link
        );
    }

    @Override
    public List<Post> list(String link) throws IOException {
        Document document = Jsoup.connect(link).get();
        Elements names = document.select(
                "#content-wrapper-forum > table.forumTable > tbody > tr > td.postslisttopic"
        );
        List<Post> posts = new ArrayList<>();
        for (int i = 3; i < names.size(); i++) {
            posts.add(detail(names.get(i).child(0).attr("href")));
        }
        return posts;
    }

    @Override
    public Post detail(String link) throws IOException {
        Document document = Jsoup.connect(link).get();
        Element name = document.selectFirst("td.messageHeader");
        Element date = document.selectFirst("tr:nth-child(3) > td");
        Element text = document.selectFirst(
                "#content-wrapper-forum > table:nth-child(3) > tbody > tr:nth-child(2) > td:nth-child(2)"
        );
        Element morePages = document.selectFirst(
                "#content-wrapper-forum > table:nth-child(2) > tbody > tr > td"
        );
        if (morePages != null && morePages.text().startsWith(
                "Топик располагается на нескольких страницах"
        )) {
            text = document.selectFirst(
                    "#content-wrapper-forum > table:nth-child(4) > tbody > tr:nth-child(2) > td:nth-child(2)"
            );
        }
        return transform(name, text, date, link);
    }
}
