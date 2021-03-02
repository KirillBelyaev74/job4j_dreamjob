package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import java.util.Collection;
import java.util.Map;

public interface Store {

    Collection<Post> findAllPosts();

    void savePost(Post post);

    Post getPostById(String stringId);

    void deletePost(String stringId);

    Collection<Candidate> findAllCandidates();

    void saveCandidates(Candidate candidate);

    Candidate getCandidateById(String stringId);

    void deleteCandidate(String stringId);

    void savePhotoCandidate(String stringId, String pathPhoto);

    Map<Integer, String> findAllPhotoCandidates();

    String getPhotoCandidate(String stringId);
}
