package com.michalapps.blebroker;

public class Greeting {

    private String id;
    private String content;

    public String getId() {
        return this.id;
    }

    public String getContent() {
        return this.content;
    }

    public String toString() {
        return "Greeting " + id;
    }

}