package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class PsqlStore implements Store {

    private final BasicDataSource pool = new BasicDataSource();
    private final static Logger LOGGER = Logger.getLogger(PsqlStore.class);

    private PsqlStore() {
        Properties properties = new Properties();
        try (InputStream inputStream = PsqlStore.class.getClassLoader().getResourceAsStream("db.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            LOGGER.error("Constructor PsqlStore " + e);
        }
        try {
            Class.forName(properties.getProperty("driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(properties.getProperty("driver"));
        pool.setUrl(properties.getProperty("url"));
        pool.setUsername(properties.getProperty("username"));
        pool.setPassword(properties.getProperty("password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Post> findAllPosts() {
        LOGGER.debug("Loading all the posts");
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
        LOGGER.debug("Return list of the posts " + posts.toString());
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
        LOGGER.debug("Received arguments post " + post.toString());
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
        LOGGER.debug("Returned post " + post.toString());
        return post;
    }

    private void updatePost(Post post) {
        LOGGER.debug("Received arguments post " + post.toString());
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
        LOGGER.debug("Updated post" + post.toString());
    }

    @Override
    public Post getPostById(String stringId) {
        LOGGER.debug("Received arguments stringId " + stringId);
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
        LOGGER.debug("Returned post " + post.toString());
        return post;
    }

    @Override
    public void deletePost(String stringId) {
        LOGGER.debug("Received arguments stringId " + stringId);
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
        LOGGER.debug("Deleted post by stringId " + stringId);
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        LOGGER.debug("Loading all the candidates");
        List<Candidate> candidates = new LinkedList<>();
        try (Connection connection = pool.getConnection();
             Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(
                    "select c.id, c.name, p.candidate_id from candidates c left join photocandidate p on (c.id = p.candidate_id) order by c.id")) {
                while (resultSet.next()) {
                    candidates.add(
                            new Candidate(
                                    resultSet.getInt("id"),
                                    resultSet.getString("name"),
                                    resultSet.getInt("candidate_id")));
                }
            }
        } catch (SQLException throwables) {
            LOGGER.error("Method findAllCandidates " + throwables);
        }
        LOGGER.debug("Returned list candidates " + candidates.toString());
        return candidates;
    }

    @Override
    public void saveCandidates(Candidate candidate) {
        if (candidate.getId() == 0) {
            createCandidate(candidate);
        } else {
            updateCandidate(candidate);
        }
    }

    private Candidate createCandidate(Candidate candidate) {
        LOGGER.debug("Received arguments candidate " + candidate.toString());
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO candidates(name) VALUES (initcap(?))",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, candidate.getName());
            preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    candidate.setId(resultSet.getInt(1));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Method createCandidate " + e);
        }
        LOGGER.debug("Returned candidate " + candidate.toString());
        return candidate;
    }

    private void updateCandidate(Candidate candidate) {
        LOGGER.debug("Received arguments candidate " + candidate.toString());
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "update candidates set name = initcap(?) where id = ?")) {
            preparedStatement.setString(1, candidate.getName());
            preparedStatement.setInt(2, candidate.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            LOGGER.error("Method updateCandidate " + throwables);
        }
        LOGGER.debug("Updated candidate " + candidate.toString());
    }

    @Override
    public Candidate getCandidateById(String stringId) {
        LOGGER.debug("Received arguments stringId " + stringId);
        int id = Integer.parseInt(stringId);
        if (id == 0) {
            LOGGER.error("Method getCandidateById has arguments stringId = " + stringId);
        }
        Candidate candidate = null;
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "select c.id, c.name, p.candidate_id from candidates c "
                             + "left join photocandidate p on (c.id = p.candidate_id) where c.id = ?")) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    candidate = new Candidate(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("candidate_id"));
                }
            }
        } catch (SQLException throwables) {
            LOGGER.error("Method getCandidateById " + throwables);
        }
        LOGGER.debug("Returned candidate " + candidate.toString());
        return candidate;
    }

    @Override
    public void deleteCandidate(String stringId) {
        LOGGER.debug("Received arguments stringId " + stringId);
        int id = 0;
        try {
            id = Integer.parseInt(stringId);
        } catch (Exception e) {
            LOGGER.error("Method deleteCandidate has arguments stringId = " + stringId);
        }
        deletePhotoCandidate(id);
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "delete from candidates where id = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            LOGGER.error("Method deleteCandidate " + throwables);
        }
        LOGGER.debug("Deleted candidate by stringId " + stringId);
    }

    @Override
    public void savePhotoCandidate(String stringId, String pathPhoto) {
        LOGGER.debug("Received arguments stringId " + stringId + " and pathPhoto " + pathPhoto);
        int id = 0;
        try {
            id = Integer.parseInt(stringId);
            if (pathPhoto == null) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            LOGGER.error("Method deleteCandidate has argument stringId = " + stringId + " and argument pathPhoto = " + pathPhoto);
        }
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "insert into photocandidate(candidate_id, pathphoto) values (?, ?)")) {
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, pathPhoto);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            LOGGER.error("Method savePhotoCandidate " + throwables);
        }
        LOGGER.debug("Saved photo of candidate by stringId " + stringId + " and pathPhoto" + pathPhoto);
    }

    @Override
    public Map<Integer, String> findAllPhotoCandidates() {
        LOGGER.debug("Loading all the photo of the candidates");
        Map<Integer, String> map = new HashMap<>();
        try (Connection connection = pool.getConnection();
             Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery("select * from photoCandidate order by candidate_id")) {
                while (resultSet.next()) {
                    map.put(resultSet.getInt("candidate_id"), resultSet.getString("pathPhoto"));
                }
            }
        } catch (SQLException throwables) {
            LOGGER.error("Method findAllPhotoCandidates " + throwables);
        }
        LOGGER.debug("Returned list photo of all the candidates " + map.toString());
        return map;
    }

    @Override
    public String getPhotoCandidate(String stringId) {
        LOGGER.debug("Received arguments stringId " + stringId);
        int id = 0;
        try {
            id = Integer.parseInt(stringId);
        } catch (Exception e) {
            LOGGER.error("Method getPhotoCandidate has arguments stringId = " + stringId);
        }
        String result = null;
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "select pathphoto from candidates c left join photocandidate p on (c.id = p.candidate_id) where c.id = ?")) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    result = resultSet.getString("pathPhoto");
                }
            }
        } catch (SQLException throwables) {
            LOGGER.error("Method getPhotoCandidate " + throwables);
        }
        LOGGER.debug("Returned path of the photo " + result);
        return result;
    }

    public void deletePhotoCandidate(int id) {
        LOGGER.debug("Received arguments id " + id);
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "delete from photoCandidate where candidate_id = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            LOGGER.error("Method deletePhotoCandidate " + throwables);
        }
        LOGGER.debug("Deleted photo of the candidate by id " + id);
    }

    @Override
    public User addUser(User user) {
        LOGGER.debug("Received argument user " + user.toString());
        try (Connection connection = pool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into consumers(name, email, password) values (initcap(?), lower(?), ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
        } catch (SQLException throwables) {
            LOGGER.error("Method addUser " + throwables);
        }
        LOGGER.debug("Returned user " + user.toString());
        return user;
    }

    @Override
    public User getUser(String email, String password) {
        LOGGER.debug("Received arguments email " + email + " and password" + password);
        User user = null;
        try (Connection connection = pool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select * from consumers where email = lower(?) and password = ?")) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"));
            }
        } catch (SQLException throwables) {
            LOGGER.error("Method getUser " + throwables);
        }
        LOGGER.debug("Returned user " + user.toString());
        return user;
    }
}