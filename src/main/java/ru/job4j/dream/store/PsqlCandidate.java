package ru.job4j.dream.store;

import org.apache.log4j.Logger;
import ru.job4j.dream.model.Candidate;

import java.sql.*;
import java.util.*;

public class PsqlCandidate extends DataBasePool implements StoreCandidate{

    public PsqlCandidate(){}

    private final static Logger LOGGER = Logger.getLogger(PsqlCandidate.class);

    private static final class Lazy {
        private static final StoreCandidate INST = new PsqlCandidate();
    }

    public static StoreCandidate instOf() {
        return PsqlCandidate.Lazy.INST;
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        List<Candidate> candidates = new LinkedList<>();
        try (Connection connection = pool.getConnection();
             Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(
                    "select c.id, c.name, p.candidate_id, t.candidate_id from candidates c " +
                            "left join photocandidate p on (c.id = p.candidate_id) " +
                            "left join city t on (c.id = t.candidate_id)")) {
                while (resultSet.next()) {
                    candidates.add(
                            new Candidate(
                                    resultSet.getInt("id"),
                                    resultSet.getString("name"),
                                    resultSet.getInt("candidate_id"),
                                    resultSet.getInt("candidate_id")));
                }
            }
        } catch (SQLException throwables) {
            LOGGER.error("Method findAllCandidates " + throwables);
        }
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
        return candidate;
    }

    private void updateCandidate(Candidate candidate) {
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "update candidates set name = initcap(?) where id = ?")) {
            preparedStatement.setString(1, candidate.getName());
            preparedStatement.setInt(2, candidate.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            LOGGER.error("Method updateCandidate " + throwables);
        }
    }

    @Override
    public Candidate getCandidateById(String stringId) {
        int id = 0;
        try {
            id = Integer.parseInt(stringId);
        } catch (Exception e) {
            LOGGER.error("Method deleteCandidate has arguments stringId = " + stringId);
        }
        Candidate candidate = null;
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "select c.id, c.name, p.candidate_id, t.candidate_id from candidates c " +
                             "left join photocandidate p on (c.id = p.candidate_id) " +
                             "left join city t on (c.id = t.candidate_id) " +
                             "where c.id = ?")) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    candidate = new Candidate(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("candidate_id"),
                            resultSet.getInt("candidate_id"));
                }
            }
        } catch (SQLException throwables) {
            LOGGER.error("Method getCandidateById " + throwables);
        }
        return candidate;
    }

    @Override
    public void deleteCandidate(String stringId) {
        int id = 0;
        try {
            id = Integer.parseInt(stringId);
        } catch (Exception e) {
            LOGGER.error("Method deleteCandidate has arguments stringId = " + stringId);
        }
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "delete from candidates where id = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            LOGGER.error("Method deleteCandidate " + throwables);
        }
    }

    @Override
    public void savePhotoCandidate(String stringId, String pathPhoto) {
        int id = 0;
        try {
            id = Integer.parseInt(stringId);
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
    }

    @Override
    public void deletePhotoAndCityOfCandidate(String stringId) {
        int id = 0;
        try {
            id = Integer.parseInt(stringId);
        } catch (Exception e) {
            LOGGER.error("Method deleteCandidate has arguments stringId = " + stringId);
        }
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "delete from photoCandidate where candidate_id = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            LOGGER.error("Method deletePhotoCandidate " + throwables);
        }
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "delete from city where candidate_id = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            LOGGER.error("Method deletePhotoCandidate " + throwables);
        }
    }

    @Override
    public Map<Integer, String> findAllPhotoOfTheCandidates() {
        Map<Integer, String> photo = new HashMap<>();
        try (Connection connection = pool.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from photoCandidate");
            while (resultSet.next()) {
                photo.put(resultSet.getInt("candidate_id"), resultSet.getString("pathPhoto"));
            }
        } catch (SQLException throwables) {
            LOGGER.error("Method findAllPhotoOfTheCandidates " + throwables);
        }
        return photo;
    }

    @Override
    public void saveCityOfCandidate(String stringId, String nameCity) {
        int id = 0;
        try {
            id = Integer.parseInt(stringId);
        } catch (Exception e) {
            LOGGER.error("Method getPhotoCandidate has arguments stringId = " + stringId);
        }
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "insert into city(candidate_id, name) values (?, initcap(?))")) {
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, nameCity);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            LOGGER.error("Method saveCityOfCandidate " + throwables);
        }
    }

    @Override
    public Map<Integer, String> findAllCityOfCandidates() {
        Map<Integer, String> cities = new HashMap<>();
        try (Connection connection = pool.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from city");
            while (resultSet.next()) {
                cities.put(resultSet.getInt("candidate_id"), resultSet.getString("name"));
            }
        } catch (SQLException throwables) {
            LOGGER.error("Method findAllCityOfCandidates " + throwables);
        }
        return cities;
    }
}
