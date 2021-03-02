package ru.job4j.dream.model;

import java.util.Objects;

public class Candidate {

    private int id;
    private String name;
    private int photoId;

    public Candidate(int id, String name, int photoId) {
        this.id = id;
        this.name = name;
        this.photoId = photoId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Candidate candidate = (Candidate) o;
        return id == candidate.id && photoId == candidate.photoId && Objects.equals(name, candidate.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, photoId);
    }

    @Override
    public String toString() {
        return "Candidate { " + " id = " + id
                + ", name = '" + name + '\''
                + ", photoId = " + photoId
                + '}';
    }
}
