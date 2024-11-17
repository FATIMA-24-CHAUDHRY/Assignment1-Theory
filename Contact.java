import java.util.ArrayList;

//  contact with ID and name
class Contact {
    private int id;
    private String name;

    public Contact(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void displayContact() {
        System.out.println("ID: " + id + ", Name: " + name);
    }
}
