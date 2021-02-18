package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.util.Collection;

public interface Store {

    Collection<Post> findAllPosts();

    void savePost(Post post);

    Post getPostById(int id);

    Collection<Candidate> findAllCandidates();

    void saveCandidates(Candidate candidate);

    Candidate getCandidateById(int id);
}
