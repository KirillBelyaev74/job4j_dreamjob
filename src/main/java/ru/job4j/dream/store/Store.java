package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Store {

    private static final Store INST = new Store();
    private static final AtomicInteger POST_ID = new AtomicInteger(4);
    private static final AtomicInteger CANDIDATE_ID = new AtomicInteger(4);
    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private Store() {
        posts.put(1, new Post(1, "Junior Java", "Вакансия на должность программиста Junior", new GregorianCalendar()));
        posts.put(2, new Post(2, "Middle Java", "Ваканисия на должность программиста Middle", new GregorianCalendar()));
        posts.put(3, new Post(3, "Senior Java", "Ваканисия на должность программиста Senior", new GregorianCalendar()));
        candidates.put(1, new Candidate(1, "Junior Java"));
        candidates.put(2, new Candidate(2, "Middle Java"));
        candidates.put(3, new Candidate(3, "Senior Java"));
    }

    public static Store instOf() {
        return INST;
    }

    public void savePost(Post post) {
        int id = POST_ID.incrementAndGet();
        post.setId(id);
        posts.put(id, post);
    }

    public void saveCandidates(Candidate candidate) {
        int id = CANDIDATE_ID.incrementAndGet();
        candidate.setId(id);
        candidates.put(id, candidate);
    }

    public Collection<Post> findAllPosts() {
        return posts.values();
    }

    public Collection<Candidate> findAllCandidates() {
        return candidates.values();
    }
}