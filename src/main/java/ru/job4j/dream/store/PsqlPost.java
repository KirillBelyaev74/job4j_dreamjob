package ru.job4j.dream.store;

import org.apache.log4j.Logger;
import ru.job4j.dream.model.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class PsqlPost extends DataBasePool implements StorePost {

    public PsqlPost() {

    }

    private final static Logger LOGGER = Logger.getLogger(PsqlPost.class);

    private static final class Lazy {
        private static final StorePost INST = new PsqlPost();
    }

    public static StorePost instOf() {
        return PsqlPost.Lazy.INST;
    }

    @Override
    public Collection<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection connection = pool.getConnection();
             Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery("select * from posts order by id")) {
                while (resultSet.next()) {
                    posts.add(
                            new Post(resultSet.getInt("id"),
                                    resultSet.getString("name"),
                                    resultSet.getString("description"),
                                    new Date(resultSet.getDate("created").getTime())));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Method findAllPosts " + e);
        }
        return posts;
    }

    @Override
    public void savePost(Post post) {
        if (post.getId() == 0) {
            createPost(post);
        } else {
            updatePost(post);
        }
    }

    private Post createPost(Post post) {
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO posts(name, description, created) VALUES (initcap(?), initcap(?), ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, post.getName());
            preparedStatement.setString(2, post.getDescription());
            preparedStatement.setTimestamp(3, new Timestamp(post.getCreated().getTime()));
            preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    post.setId(resultSet.getInt(1));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Method createPost " + e);
        }
        return post;
    }

    private void updatePost(Post post) {
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "update posts set name = initcap(?), description = initcap(?), created = ? where id = ?")) {
            preparedStatement.setString(1, post.getName());
            preparedStatement.setString(2, post.getDescription());
            preparedStatement.setTimestamp(3, new Timestamp(post.getCreated().getTime()));
            preparedStatement.setInt(4, post.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            LOGGER.error("Method updatePost " + throwables);
        }
    }

    @Override
    public Post getPostById(String stringId) {
        int id = 0;
        try {
            id = Integer.parseInt(stringId);
        } catch (Exception e) {
            LOGGER.error("Method getPostById has arguments stringId = " + stringId);
        }
        Post post = null;
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "select * from posts where id = ?")) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    post = new Post(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getDate("created"));
                }
            }
        } catch (SQLException throwables) {
            LOGGER.error("Method getPostById " + throwables);
        }
        return post;
    }

    @Override
    public void deletePost(String stringId) {
        int id = 0;
        try {
            id = Integer.parseInt(stringId);
        } catch (Exception e) {
            LOGGER.error("Method deletePost has arguments stringId = " + stringId);
        }
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "delete from posts where id = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            LOGGER.error("Method deletePost " + throwables);
        }
    }
}
