package ch.hsrmarket.android.model;

import com.google.gson.annotations.SerializedName;

public class Purchase {

    @SerializedName("id")
    private int id;

    @SerializedName("article")
    private Article article;

    @SerializedName("buyer")
    private Account buyer;

    @SerializedName("seller")
    private Account seller;

    @SerializedName("purchaseDate")
    private String date;

    @SerializedName("completed")
    private boolean completed;

    public Purchase(int id, Article article, Account buyer, Account seller, String date, boolean completed) {
        this.id = id;
        this.article = article;
        this.buyer = buyer;
        this.seller = seller;
        this.date = date;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Account getBuyer() {
        return buyer;
    }

    public void setBuyer(Account buyer) {
        this.buyer = buyer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Account getSeller() {
        return seller;
    }

    public void setSeller(Account seller) {
        this.seller = seller;
    }
}
