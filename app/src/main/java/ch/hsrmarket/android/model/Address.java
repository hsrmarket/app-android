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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (zip != address.zip) return false;
        if (!street.equals(address.street)) return false;
        if (!streetNo.equals(address.streetNo)) return false;
        return city.equals(address.city);

    }

    @Override
    public int hashCode() {
        int result = street.hashCode();
        result = 31 * result + streetNo.hashCode();
        result = 31 * result + zip;
        result = 31 * result + city.hashCode();
        return result;
    }
}
