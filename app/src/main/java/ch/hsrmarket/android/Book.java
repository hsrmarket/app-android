package ch.hsrmarket.android;

import com.google.gson.annotations.SerializedName;

/**
 * Created by athanabalasingam on 05.04.17.
 */

public class Book {

    @SerializedName("id")
    private int id;

    @SerializedName("iban")
    private String ISBN;

    @SerializedName("author")
    private String author;

    public Book(int id, String isbn, String author){
        this.id = id;
        this.ISBN = isbn;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
