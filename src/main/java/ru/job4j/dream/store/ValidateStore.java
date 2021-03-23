package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.util.*;

public class ValidateStore implements StoreCandidate, StorePost {

    private final Map<Integer, Post> posts = new HashMap<>();
    private final Map<Integer, Candidate> candidates = new HashMap<>();
    private int idPosts = 0;
    private int idCandidate = 0;

    @Override
    public Collection<Post> findAllPosts() {
        return new ArrayList<>(this.posts.values());
    }

    @Override
    public void savePost(Post post) {
        post.setId(this.idPosts++);
        this.posts.put(post.getId(), post);
    }

    @Override
    public void saveCandidates(Candidate candidate) {
        candidate.setId(idCandidate++);
        candidates.put(candidate.getId(), candidate);
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        return new ArrayList<>(candidates.values());
    }

    @Override
    public Post getPostById(String stringId) {
        return null;
    }

    @Override
    public void deletePost(String stringId) {

    }

    @Override
    public Candidate getCandidateById(String stringId) {
        return null;
    }

    @Override
    public void deleteCandidate(String stringId) {

    }

    @Override
    public void savePhotoCandidate(String stringId, String pathPhoto) {

    }

    @Override
    public Map<Integer, String> findAllPhotoOfTheCandidates() {
        return null;
    }

    @Override
    public void deletePhotoAndCityOfCandidate(String stringId) {

    }

    @Override
    public void saveCityOfCandidate(String stringId, String nameCity) {

    }

    @Override
    public Map<Integer, String> findAllCityOfCandidates() {
        return null;
    }
}
