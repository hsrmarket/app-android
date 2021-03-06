package ch.hsrmarket.android.model;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Article {

    public enum Type{
        @SerializedName("book")
        BOOK("book"),

        @SerializedName("electronic")
        ELECTRONIC_DEVICE("electronic"),

        @SerializedName("office supply")
        OFFICE_SUPPLY("office supply"),

        @SerializedName("other")
        OTHER("other"),

        UNKOWN("null");

        private final String name;

        Type(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
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

    @SerializedName("createdby")
    private int createdby;

    private int purchaseId;
    private int sellerId;
    private int buyerId;

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

    public Article(String name, double price, int condition, String description, Type type) {
        this.name = name;
        this.price = price;
        this.condition = condition;
        this.description = description;
        this.type = type;
        this.imagePath = "";

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(calendar.getTime());

        this.createdAt = formattedDate;
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

    public String getPrice() {
        return String.format( "%.2f", price );
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCondition() {
        String retVal = "";

        for(int i = 0 ; i < condition; i++){
            retVal += "\uD83D\uDD25";
        }

        return retVal;
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

    public void setPurchaseId(int purchaseId){
        this.purchaseId = purchaseId;
    }

    public int getPurchaseId(){
       return purchaseId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public void setCreatedBy(int createdby){
        this.createdby = createdby;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", condition=" + condition +
                ", description='" + description + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", type=" + type +
                ", createdBy=" + createdby +
                ", purchaseId=" + purchaseId +
                '}';
    }
}
