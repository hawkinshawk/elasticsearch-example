package com.mkyong.book.model;

public class Issue {

    private String year;

    @Override
    public String toString() {
        return "Issue{" +
                "year='" + year + '\'' +
                '}';
    }

    public Issue() {
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Issue(String jahr) {
        this.year = jahr;
    }
}
