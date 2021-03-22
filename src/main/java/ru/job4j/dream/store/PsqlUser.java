package ru.job4j.dream.store;

import org.apache.log4j.Logger;
import ru.job4j.dream.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PsqlUser extends DataBasePool implements StoreUser {

    public PsqlUser(){}

    private final static Logger LOGGER = Logger.getLogger(PsqlUser.class);

    private static final class Lazy {
        private static final StoreUser INST = new PsqlUser();
    }

    public static StoreUser instOf() {
        return PsqlUser.Lazy.INST;
    }

    @Override
    public User saveUser(User user) {
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
        return user;
    }

    @Override
    public User getUser(String email, String password) {
        User user = null;
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "select * from consumers where email = lower(?) and password = ?")) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user = new User(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password"));
                }
            }
        } catch (SQLException throwables) {
            LOGGER.error("Method getUser " + throwables);
        }
        return user;
    }

    @Override
    public boolean checkLiveUser(String email) {
        boolean result = false;
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "select * from consumers where email = lower(?)")) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                result = resultSet.next();
            }
        } catch (SQLException throwables) {
            LOGGER.error("Method checkLiveUser " + throwables);
        }
        return result;
    }
}
