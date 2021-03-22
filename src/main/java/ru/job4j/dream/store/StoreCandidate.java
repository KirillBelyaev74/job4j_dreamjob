package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;

import java.util.Collection;
import java.util.Map;

public interface StoreCandidate {

    Collection<Candidate> findAllCandidates();

    void saveCandidates(Candidate candidate);

    Candidate getCandidateById(String stringId);

    void deleteCandidate(String stringId);

    void savePhotoCandidate(String stringId, String pathPhoto);

    Map<Integer, String> findAllPhotoOfTheCandidates();

    void deletePhotoAndCityOfCandidate(String stringId);

    void saveCityOfCandidate(String stringId, String nameCity);

    Map<Integer, String> findAllCityOfCandidates();
}