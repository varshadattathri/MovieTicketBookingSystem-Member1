
public abstract class User {

    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Every user type defines its own role
    public abstract String getRole();
}
