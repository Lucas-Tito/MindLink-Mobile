package com.titolucas.mindlink;
public class Psychologist {
    private final String name;
    private final String specialty;
    private final double rating;

    public Psychologist(String name, String specialty, double rating) {
        this.name = name;
        this.specialty = specialty;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public double getRating() {
        return rating;
    }
}
