package org.example;

public class Lecturer {
    private String id;
    private String name;

    public Lecturer(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String toString() {
        return id + " - " + name;
    }
}

