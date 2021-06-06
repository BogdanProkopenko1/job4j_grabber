package ru.job4j.grabber;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private Connection cnn;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("rabbit.db-driver-class-name"));
            cnn = DriverManager.getConnection(
                    cfg.getProperty("rabbit.db-url"),
                    cfg.getProperty("rabbit.db-username"),
                    cfg.getProperty("rabbit.db-password")
                    );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement statement = cnn.prepareStatement(
                "insert into post(name, text, link, created) values(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setString(1, post.getName());
            statement.setString(2, post.getText());
            statement.setString(3, post.getLink());
            statement.setTimestamp(4, Timestamp.valueOf(post.getCreatedDate()));
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        ResultSet resultSet;
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement statement = cnn.prepareStatement(
                "select * from post"
        )) {
            statement.execute();
            resultSet = statement.getResultSet();
            while (resultSet.next()) {
                Post post = new Post(
                        resultSet.getTimestamp("created").toLocalDateTime(),
                        resultSet.getString("name"),
                        resultSet.getString("text"),
                        resultSet.getString("link")
                );
                post.setId(resultSet.getInt("id"));
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post findById(String id) {
        Post post = new Post();
        ResultSet resultSet;
        try (PreparedStatement statement = cnn.prepareStatement(
                "select * from post where id=?"
        )) {
            statement.setInt(1, Integer.parseInt(id));
            statement.execute();
            resultSet = statement.getResultSet();
            resultSet.next();
            post = new Post(
                    resultSet.getTimestamp("created").toLocalDateTime(),
                    resultSet.getString("name"),
                    resultSet.getString("text"),
                    resultSet.getString("link")
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }
}