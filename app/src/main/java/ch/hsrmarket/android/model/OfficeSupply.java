package ch.hsrmarket.android.model;

public class OfficeSupply extends Article {

    public OfficeSupply(int id, String name, double price, int condition, String description, String createdAt, String imagePath, Type type) {
        super(id, name, price, condition, description, createdAt, imagePath, type);
    }

    public OfficeSupply(String name, double price, int condition, String description) {
        super(name, price, condition, description, Type.OFFICE_SUPPLY);
    }

    @Override
    public String toString() {
        return "OfficeSupply{} " + super.toString();
    }
}
