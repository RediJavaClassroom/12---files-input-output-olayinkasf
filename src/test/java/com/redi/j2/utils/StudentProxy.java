package com.redi.j2.utils;

import java.time.LocalDate;
import java.util.UUID;

public class StudentProxy extends ReflectionProxy {

    public StudentProxy(Object... args) {
        super(args);
    }

    public StudentProxy(Object target) {
        super(target);
    }

    @Override
    public String getTargetClassName() {
        return "com.redi.j2.Student";
    }

    public UUID getId() {
        return invokeMethod("getId", new Class[] {});
    }

    public String getFirstName() {
        return invokeMethod("getFirstName", new Class[] {});
    }

    public String getLastName() {
        return invokeMethod("getLastName", new Class[] {});
    }

    public int getHeight() {
        return invokeMethod("getHeight", new Class[] {});
    }

    public int getWeight() {
        return invokeMethod("getWeight", new Class[] {});
    }

    public LocalDate getDateOfBirth() {
        return invokeMethod("getDateOfBirth", new Class[] {});
    }

    public UUID id() {
        return getPropertyValue("id");
    }

    public String firstName() {
        return getPropertyValue("firstName");
    }

    public String lastName() {
        return getPropertyValue("lastName");
    }

    public int height() {
        return getPropertyValue("height");
    }

    public int weight() {
        return getPropertyValue("weight");
    }

    public LocalDate dateOfBirth() {
        return getPropertyValue("dateOfBirth");
    }

    @Override
    public boolean equals(Object o) {
        return invokeMethod("equals", new Class[] {Object.class}, o);
    }

    @Override
    public int hashCode() {
        return invokeMethod("hashCode", new Class[] {});
    }

    @Override
    public String toString() {
        return invokeMethod("toString", new Class[] {});
    }
}
