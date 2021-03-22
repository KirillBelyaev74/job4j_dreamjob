package ru.job4j.dream.store;

import ru.job4j.dream.model.Post;

import java.util.Collection;

public interface StorePost {

    Collection<Post> findAllPosts();

    void savePost(Post post);

    Post getPostById(String stringId);

    void deletePost(String stringId);
}
