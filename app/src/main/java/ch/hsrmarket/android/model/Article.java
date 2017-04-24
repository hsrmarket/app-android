package ch.hsrmarket.android.model;

import com.google.gson.annotations.SerializedName;

public class Article {

    public enum Type{
        @SerializedName("book") BOOK,
        @SerializedName("electronic") ELECTRONIC_DEVICE,
        @SerializedName("office supply") OFFICE_SUPPLY,
        @SerializedName("other") OTHER,
        UNKOWN
    }

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private double price;

    @SerializedName("condition")
    private int condition;

    @SerializedName("description")
    private String description;

    @SerializedName("creationDate")
    private String createdAt;

    @SerializedName("image")
    private String imagePath;

    @SerializedName("type")
    private Type type;

    public Article(int id, String name, double price, int condition, String description, String createdAt, String imagePath, Type type) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.condition = condition;
        this.description = description;
        this.createdAt = createdAt;
        this.imagePath = imagePath;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Type getType() {
        // to avoid NullPointerException
        if(type == null){
            type = Type.UNKOWN;
        }
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
