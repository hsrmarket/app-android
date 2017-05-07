package ch.hsrmarket.android.model;

import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("street")
    private String street;

    @SerializedName("streetNr")
    private String streetNo;

    @SerializedName("zip")
    private int zip;

    @SerializedName("city")
    private String city;

    public Address(String street, String streetNo, int zip, String city) {
        this.street = street;
        this.streetNo = streetNo;
        this.zip = zip;
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNo() {
        return streetNo;
    }

    public void setStreetNo(String streetNo) {
        this.streetNo = streetNo;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
