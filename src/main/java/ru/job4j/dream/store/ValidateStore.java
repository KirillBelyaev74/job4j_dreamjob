package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.util.*;

public class ValidateStore implements Store {

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
    public Post getPostById(String stringId) {
        return null;
    }

    @Override
    public void deletePost(String stringId) {

    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        return new ArrayList<>(candidates.values());
    }

    @Override
    public void saveCandidates(Candidate candidate) {
        candidate.setId(idCandidate++);
        candidates.put(candidate.getId(), candidate);
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
    public Map<Integer, String> findAllPhotoCandidates() {
        return null;
    }

    @Override
    public String getPhotoCandidate(String stringId) {
        return null;
    }

    @Override
    public User addUser(User user) {
        return user;
    }

    @Override
    public User getUser(String email, String password) {
        return null;
    }

    @Override
    public boolean checkLiveUser(String email) {
        return false;
    }
}
