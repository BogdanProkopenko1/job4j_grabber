package ru.job4j.grabber;

import java.util.Date;

public class Post {

    private Date createdDate;
    private String author;
    private String link;
    private String msg;
    private int views;
    private int answers;

    public Post(Date createdDate, String author, String link, String msg, int views, int answers) {
        this.createdDate = createdDate;
        this.author = author;
        this.link = link;
        this.msg = msg;
        this.views = views;
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "Post{" +
                "createdDate=" + createdDate +
                ", author='" + author + '\'' +
                ", link='" + link + '\'' +
                ", msg='" + msg + '\'' +
                ", views=" + views +
                ", answers=" + answers +
                '}';
    }
}