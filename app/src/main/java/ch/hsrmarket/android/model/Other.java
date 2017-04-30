package ch.hsrmarket.android.model;

public class Other extends Article {

    public Other(int id, String name, double price, int condition, String description, String createdAt, String imagePath, Type type) {
        super(id, name, price, condition, description, createdAt, imagePath, type);
    }

    @Override
    public String toString() {
        return "Other{} " + super.toString();
    }
}
