package ru.job4j.cinema.model;

import java.util.Objects;

/**
 * @author Roman Rusanov
 * @version 0.1
 * @since 16.12.2020
 * email roman9628@gmail.com
 * The class describe model place in cinema.
 */
public class Place {
    /**
     * The field contain row value.
     */
    private int row;
    /**
     * The field contain number of place in row.
     */
    private int number;

    /**
     * The default constructor.
     * @param row row
     * @param number place
     */
    public Place(int row, int number) {
        this.row = row;
        this.number = number;
    }

    /**
     * The getter.
     * @return int
     */
    public int getRow() {
        return row;
    }

    /**
     * The setter.
     * @param row int
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * The getter.
     * @return int
     */
    public int getNumber() {
        return number;
    }

    /**
     * The setter.
     * @param number int
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * The method override equals.
     * @param o Object to compare.
     * @return if fields equals return true, otherwise false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return row == place.row && number == place.number;
    }

    /**
     * The method override hashcode.
     * @return generate hash using fields.
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, number);
    }

    /**
     * The override toString method.
     * @return String.
     */
    @Override
    public String toString() {
        return "Place{" +
                "row=" + row +
                ", number=" + number +
                '}';
    }
}