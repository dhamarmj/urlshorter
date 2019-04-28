package edu.pucmm.dhamarmj.Encapsulation;

import java.util.List;

public class Groupby {
    private String name;
    private int value;


    // Generic function to find the index of an element in an object array in Java


    public Groupby() {
    }

    public Groupby(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
