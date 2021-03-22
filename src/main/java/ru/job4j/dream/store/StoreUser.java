package ru.job4j.dream.store;

import ru.job4j.dream.model.User;

public interface StoreUser {

    User saveUser(User user);

    User getUser(String email, String password);

    boolean checkLiveUser(String email);
}
