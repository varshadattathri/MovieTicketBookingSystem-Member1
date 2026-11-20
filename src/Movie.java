
public class Movie {

    private int id;
    private int duration;
    private int price;
    private String name;
    private String genre;

    // Constructor to initialize movie details
    public Movie(int id, String name, String genre,
                 int duration, int price) {

        this.id = id;
        this.name = name;
        this.genre = genre;
        this.duration = duration;
        this.price = price;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGenre() {
        return genre;
    }

    public int getDuration() {
        return duration;
    }

    public int getPrice() {
        return price;
    }

    // Setter methods
    // Setter methods with validation
public void setName(String name) {
    if (name == null || name.trim().isEmpty()) {
        throw new IllegalArgumentException("Movie name cannot be empty.");
    }
    this.name = name;
}

public void setGenre(String genre) {
    if (genre == null || genre.trim().isEmpty()) {
        throw new IllegalArgumentException("Genre cannot be empty.");
    }
    this.genre = genre;
}

public void setDuration(int duration) {
    if (duration <= 0) {
        throw new IllegalArgumentException("Duration must be positive.");
    }
    this.duration = duration;
}

public void setPrice(int price) {
    if (price <= 0) {
        throw new IllegalArgumentException("Price must be positive.");
    }
    this.price = price;
}
    // Displays movie details
    public void display() {
        System.out.println(id + " | " + name + " | " + genre
                + " | " + duration + " mins | Rs." + price);
    }
}
