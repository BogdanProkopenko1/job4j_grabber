package ru.job4j.grabber;

import java.time.LocalDateTime;

public class Post {

    private int id;
    private LocalDateTime createdDate;
    private String name;
    private String text;
    private String link;

    public Post(LocalDateTime createdDate, String author, String link, String msg) {
        this.createdDate = createdDate;
        this.name = author;
        this.text = link;
        this.link = msg;
    }

    public Post() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}