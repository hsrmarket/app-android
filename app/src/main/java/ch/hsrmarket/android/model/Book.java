package ch.hsrmarket.android.model;

import com.google.gson.annotations.SerializedName;

public class Book extends Article {

    @SerializedName("isbn")
    private String ISBN;

    @SerializedName("author")
    private String author;

    @SerializedName("publisher")
    private String publisher;

    public Book(int id, String name, double price, int condition, String description, String createdAt, String imagePath, Type type, String ISBN, String author, String publisher) {
        super(id, name, price, condition, description, createdAt, imagePath, type);
        this.ISBN = ISBN;
        this.author = author;
        this.publisher = publisher;
    }

    public Book(String name, double price, int condition, String description, String ISBN, String author, String publisher) {
        super(name, price, condition, description, Type.BOOK);
        this.ISBN = ISBN;
        this.author = author;
        this.publisher = publisher;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return "Book{" +
                "ISBN='" + ISBN + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                "} " + super.toString();
    }
}
