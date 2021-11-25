package com.redi.j2;

import java.time.LocalDate;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class Student {
    private final UUID id;
    private final String firstName;
    private final String lastName;
    private final int height;
    private final int weight;
    private final LocalDate dateOfBirth;

    public Student(
            UUID id,
            String firstName,
            String lastName,
            int height,
            int weight,
            LocalDate dateOfBirth) {
        this.id = Objects.requireNonNull(id);
        this.firstName = Objects.requireNonNull(firstName);
        this.lastName = Objects.requireNonNull(lastName);
        this.height = height;
        this.weight = weight;
        this.dateOfBirth = Objects.requireNonNull(dateOfBirth);

        if (firstName.isBlank()) throw new BadNameException();
        if (lastName.isBlank()) throw new BadNameException();
        if (weight <= 0) throw new BadWeightException();
        if (height <= 0) throw new BadHeightException();
        if (dateOfBirth.isAfter(LocalDate.now())) throw new BadDateException();
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return height == student.height
                && weight == student.weight
                && Objects.equals(id, student.id)
                && Objects.equals(firstName, student.firstName)
                && Objects.equals(lastName, student.lastName)
                && Objects.equals(dateOfBirth, student.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, height, weight, dateOfBirth);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Student.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("firstName='" + firstName + "'")
                .add("lastName='" + lastName + "'")
                .add("height=" + height)
                .add("weight=" + weight)
                .add("dateOfBirth=" + dateOfBirth)
                .toString();
    }
}
