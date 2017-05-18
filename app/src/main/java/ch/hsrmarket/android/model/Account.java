package ch.hsrmarket.android.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import ch.hsrmarket.android.api.GsonClient;

public class Account {

    @SerializedName("id")
    private int id;

    @SerializedName("studentId")
    private int studentId;

    @SerializedName("firstname")
    private String firstName;

    @SerializedName("lastname")
    private String lastName;

    @SerializedName("address")
    private Address address;

    @SerializedName("email")
    private String email;

    @SerializedName("telephone")
    private String phone;

    @SerializedName("password")
    private String password;

    @SerializedName("admin")
    private boolean admin;

    public Account(int id, int studentId, String firstName, String lastName, Address address, String email, String phone, String password, boolean admin) {
        this.id = id;
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.admin = admin;
    }

    public Account(int studentId, String firstName, String lastName, Address address, String phone, String email, String password) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.admin = false;
    }

    public Account(String email, String password){
        this.email = email;
        this.password = password;
    }

    public Account(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", studentId=" + studentId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address=" + address +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", admin=" + admin +
                '}';
    }

    public String getJsonObject(){
        Gson gson = GsonClient.getClient();
        return  gson.toJson(this);
    }

    public static Account makeAccount(String json){
        Gson gson = GsonClient.getClient();
        return gson.fromJson(json,Account.class);
    }

    public String getFullName(){
        return this.firstName + " " + this.lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (id != account.id) return false;
        if (studentId != account.studentId) return false;
        if (admin != account.admin) return false;
        if (!firstName.equals(account.firstName)) return false;
        if (!lastName.equals(account.lastName)) return false;
        if (!address.equals(account.address)) return false;
        if (!email.equals(account.email)) return false;
        if (!phone.equals(account.phone)) return false;
        return password.equals(account.password);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + studentId;
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + address.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + phone.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + (admin ? 1 : 0);
        return result;
    }
}
