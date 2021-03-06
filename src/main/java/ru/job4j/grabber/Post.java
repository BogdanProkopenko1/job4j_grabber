package ru.job4j.grabber;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {

    private int id;
    private LocalDateTime createdDate;
    private String name;
    private String text;
    private String link;

    public Post(LocalDateTime createdDate, String name, String text, String link) {
        this.createdDate = createdDate;
        this.name = name;
        this.text = text;
        this.link = link;
    }

    public Post() { }

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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Post)) {
            return false;
        }
        Post post = (Post) o;
        return  Objects.equals(getName(), post.getName())
                && Objects.equals(getText(), post.getText())
                && Objects.equals(getLink(), post.getLink());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCreatedDate(), getName(), getText(), getLink());
    }

    @Override
    public String toString() {
        return "Post{"
                + "id=" + id
                + ", createdDate=" + createdDate
                + ", name='" + name + '\''
                + ", text='" + text + '\''
                + ", link='" + link + '\''
                + '}';
    }
}