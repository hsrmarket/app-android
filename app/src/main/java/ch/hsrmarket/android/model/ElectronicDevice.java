package ch.hsrmarket.android.model;


import com.google.gson.annotations.SerializedName;

public class ElectronicDevice extends Article {

    @SerializedName("producer")
    private String producer;

    @SerializedName("model")
    private String model;

    public ElectronicDevice(int id, String name, double price, int condition, String description, String createdAt, String imagePath, String type, String producer, String model) {
        super(id, name, price, condition, description, createdAt, imagePath, type);
        this.producer = producer;
        this.model = model;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}

