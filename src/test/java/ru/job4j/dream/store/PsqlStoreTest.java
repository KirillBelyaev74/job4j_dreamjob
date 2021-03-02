package ru.job4j.dream.store;

import org.junit.Test;
import ru.job4j.dream.model.*;
import java.util.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


public class PsqlStoreTest {

    @Test
    public void whenAddPostsAndGetAllThePostsAndPostById() {
        Store store = PsqlStore.instOf();
        Post one = new Post(0, "one", "first", new Date());
        Post two = new Post(0, "two", "second", new Date());
        store.savePost(one);
        store.savePost(two);
        List<Post> posts = new ArrayList<>(store.findAllPosts());
        assertThat(posts.get(0).getName(), is("One"));
        assertThat(posts.get(1).getName(), is("Two"));
        assertThat(store.getPostById("1").getName(), is("One"));
    }

    @Test
    public void whenAddCandidatesAndGetAllTheCandidatesAndCandidateById() {
        Store store = PsqlStore.instOf();
        Candidate one = new Candidate(0, "1", -1);
        Candidate two = new Candidate(0, "2", -1);
        store.saveCandidates(one);
        store.saveCandidates(two);
        List<Candidate> candidates = new ArrayList<>(store.findAllCandidates());
        assertThat(candidates.get(0).getName(), is("1"));
        assertThat(candidates.get(1).getName(), is("2"));
        assertThat(store.getCandidateById("1").getName(), is("1"));
    }
}
