
import java.util.*;

// Custom exception classes
class MovieNotFoundException extends Exception {
    MovieNotFoundException(String msg) {
        super(msg);
    }
}

class SeatNotAvailableException extends Exception {
    SeatNotAvailableException(String msg) {
        super(msg);
    }
}

class BookingNotFoundException extends Exception {
    BookingNotFoundException(String msg) {
        super(msg);
    }
}

// Abstract class demonstrating abstraction
abstract class User {
    private String name;

    User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    abstract String getRole();
}

// Inheritance and method overriding
class Customer extends User {
    Customer(String name) {
        super(name);
    }

    @Override
    String getRole() {
        return "Customer";
    }
}

class Admin extends User {
    Admin(String name) {
        super(name);
    }

    @Override
    String getRole() {
        return "Administrator";
    }
}

// Movie class demonstrating encapsulation
class Movie {
    private int id, duration, price;
    private String name, genre;

    Movie(int id, String name, String genre, int duration, int price) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.duration = duration;
        this.price = price;
    }

    int getId() { return id; }
    String getName() { return name; }
    String getGenre() { return genre; }
    int getDuration() { return duration; }
    int getPrice() { return price; }

    void setName(String name) { this.name = name; }
    void setGenre(String genre) { this.genre = genre; }
    void setDuration(int duration) { this.duration = duration; }
    void setPrice(int price) { this.price = price; }

    // Displays movie information
    void display() {
        System.out.println(id + " | " + name + " | " + genre
                + " | " + duration + " mins | Rs." + price);
    }
}

// Stores booking information
class Booking {
    private int bookingId, movieId;
    private String customer, movieName, date, time;
    private ArrayList<Integer> seats;
    private double total;

    Booking(int bookingId, String customer, Movie movie,
            String date, String time, ArrayList<Integer> seats) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.movieId = movie.getId();
        this.movieName = movie.getName();
        this.date = date;
        this.time = time;
        this.seats = new ArrayList<>(seats);
        this.total = movie.getPrice() * seats.size();
    }

    int getBookingId() { return bookingId; }
    int getMovieId() { return movieId; }
    String getDate() { return date; }
    String getTime() { return time; }
    ArrayList<Integer> getSeats() { return seats; }
    double getTotal() { return total; }

    void display() {
        System.out.println("\nBooking ID: " + bookingId);
        System.out.println("Customer: " + customer);
        System.out.println("Movie: " + movieName);
        System.out.println("Date: " + date);
        System.out.println("Time: " + time);
        System.out.println("Seats: " + seats);
        System.out.println("Total: Rs." + total);
    }
}

public class MovieTicketBooking {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<Movie> movies = new ArrayList<>();
    static ArrayList<Booking> bookings = new ArrayList<>();

    // Stores occupied seats for each movie, date and showtime
    static HashMap<String, HashSet<Integer>> occupiedSeats = new HashMap<>();

    static int nextBookingId = 101;

    // Adds initial movies
    static void addSampleMovies() {
        movies.add(new Movie(1, "Interstellar", "Sci-Fi", 169, 180));
        movies.add(new Movie(2, "Inception", "Thriller", 148, 150));
        movies.add(new Movie(3, "Avatar", "Adventure", 192, 200));
        movies.add(new Movie(4, "Avengers", "Action", 181, 250));
    }

    // Searches for a movie using its ID
    static Movie findMovie(int id) throws MovieNotFoundException {
        for (Movie m : movies) {
            if (m.getId() == id)
                return m;
        }
        throw new MovieNotFoundException("Movie not found!");
    }

    // COMMON: Displays available movies
    static void displayMovies() {
        if (movies.isEmpty()) {
            System.out.println("No movies available!");
            return;
        }

        System.out.println("\nID | Name | Genre | Duration | Price");
        for (Movie m : movies)
            m.display();
    }

    // Displays show timings and returns the selected time
    static String getShowTime() {
        System.out.println("\n1. 10:00 AM");
        System.out.println("2. 2:00 PM");
        System.out.println("3. 6:00 PM");
        System.out.println("4. 9:00 PM");
        System.out.print("Select Show: ");

        switch (sc.nextInt()) {
            case 1: return "10:00 AM";
            case 2: return "2:00 PM";
            case 3: return "6:00 PM";
            case 4: return "9:00 PM";
            default: return null;
        }
    }

    // COMMON: Selects seats and checks availability
    static ArrayList<Integer> selectSeats(String key)
            throws SeatNotAvailableException {

        HashSet<Integer> booked = occupiedSeats.get(key);

        if (booked == null)
            booked = new HashSet<>();

        ArrayList<Integer> selected = new ArrayList<>();
        HashSet<Integer> temporary = new HashSet<>();

        System.out.println("Available seats: 1 to 20");
        System.out.println("Booked seats: " + booked);

        System.out.print("Enter number of seats: ");
        int count = sc.nextInt();

        if (count <= 0 || count > 20)
            throw new SeatNotAvailableException("Invalid seat quantity!");

        for (int i = 0; i < count; i++) {
            System.out.print("Enter seat number: ");
            int seat = sc.nextInt();

            if (seat < 1 || seat > 20 ||
                    booked.contains(seat) || !temporary.add(seat)) {
                throw new SeatNotAvailableException(
                        "Seat " + seat + " is unavailable!");
            }

            selected.add(seat);
        }

        // Reserve seats after validating all selections
        occupiedSeats.put(key, booked);
        booked.addAll(selected);

        return selected;
    }

    // COMMON: Books movie tickets
    static void bookTickets() {
        try {
            if (movies.isEmpty()) {
                System.out.println("No movies available!");
                return;
            }

            sc.nextLine();
            System.out.print("Customer Name: ");
            String name = sc.nextLine();

            if (name.trim().isEmpty()) {
                System.out.println("Invalid customer name!");
                return;
            }

            displayMovies();
            System.out.print("Enter Movie ID: ");
            Movie movie = findMovie(sc.nextInt());

            sc.nextLine();
            System.out.print("Enter Date (DD/MM/YYYY): ");
            String date = sc.nextLine();

            if (!date.matches("\\d{2}/\\d{2}/\\d{4}")) {
                System.out.println("Invalid date format!");
                return;
            }

            String time = getShowTime();

            if (time == null) {
                System.out.println("Invalid showtime!");
                return;
            }

            String key = movie.getId() + "|" + date + "|" + time;
            ArrayList<Integer> seats = selectSeats(key);

            Booking b = new Booking(nextBookingId++, name,
                    movie, date, time, seats);

            bookings.add(b);

            System.out.println("\nBOOKING SUCCESSFUL!");
            b.display();

        } catch (MovieNotFoundException | SeatNotAvailableException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // COMMON: Displays all bookings
    static void viewBookings() {
        if (bookings.isEmpty()) {
            System.out.println("No bookings available!");
            return;
        }

        for (Booking b : bookings)
            b.display();
    }

    // COMMON: Cancels booking and releases seats
    static void cancelBooking() {
        try {
            System.out.print("Enter Booking ID: ");
            int id = sc.nextInt();

            Booking found = null;

            for (Booking b : bookings) {
                if (b.getBookingId() == id) {
                    found = b;
                    break;
                }
            }

            if (found == null)
                throw new BookingNotFoundException("Booking not found!");

            String key = found.getMovieId() + "|" +
                    found.getDate() + "|" + found.getTime();

            HashSet<Integer> seats = occupiedSeats.get(key);

            if (seats != null)
                seats.removeAll(found.getSeats());

            bookings.remove(found);
            System.out.println("Booking cancelled successfully!");

        } catch (BookingNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // COMMON: Generates revenue report
    static void revenueReport() {
        double revenue = 0;
        int totalTickets = 0;

        for (Booking b : bookings) {
            revenue += b.getTotal();
            totalTickets += b.getSeats().size();
        }

        System.out.println("\n===== REVENUE REPORT =====");
        System.out.println("Total Bookings: " + bookings.size());
        System.out.println("Tickets Sold: " + totalTickets);
        System.out.println("Total Revenue: Rs." + revenue);
    }

    // MEMBER 1: Adds a new movie
    static void addMovie() {
        System.out.print("Enter Movie ID: ");
        int id = sc.nextInt();

        for (Movie m : movies) {
            if (m.getId() == id) {
                System.out.println("Movie ID already exists!");
                return;
            }
        }

        sc.nextLine();
        System.out.print("Movie Name: ");
        String name = sc.nextLine();

        System.out.print("Genre: ");
        String genre = sc.nextLine();

        System.out.print("Duration: ");
        int duration = sc.nextInt();

        System.out.print("Ticket Price: ");
        int price = sc.nextInt();

        if (id <= 0 || name.trim().isEmpty() ||
                genre.trim().isEmpty() || duration <= 0 || price <= 0) {
            System.out.println("Invalid movie details!");
            return;
        }

        movies.add(new Movie(id, name, genre, duration, price));
        System.out.println("Movie added successfully!");
    }

    // MEMBER 1: Removes a movie
    static void removeMovie() {
        try {
            System.out.print("Enter Movie ID: ");
            Movie m = findMovie(sc.nextInt());

            for (Booking b : bookings) {
                if (b.getMovieId() == m.getId()) {
                    System.out.println("Cannot remove a movie with active bookings!");
                    return;
                }
            }

            movies.remove(m);
            System.out.println("Movie removed successfully!");

        } catch (MovieNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    // MEMBER 1: Searches movie by name
    static void searchMovie() {
        sc.nextLine();
        System.out.print("Enter Movie Name: ");
        String name = sc.nextLine().toLowerCase();

        boolean found = false;

        for (Movie m : movies) {
            if (m.getName().toLowerCase().contains(name)) {
                m.display();
                found = true;
            }
        }

        if (!found)
            System.out.println("Movie not found!");
    }

    // MEMBER 1: Updates movie information
    static void updateMovie() {
        try {
            System.out.print("Enter Movie ID: ");
            Movie m = findMovie(sc.nextInt());

            System.out.println("1. Update Name");
            System.out.println("2. Update Genre");
            System.out.println("3. Update Duration");
            System.out.println("4. Update Price");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("New Name: ");
                    String name = sc.nextLine();
                    if (name.trim().isEmpty()) {
                        System.out.println("Invalid name!");
                        return;
                    }
                    m.setName(name);
                    break;

                case 2:
                    System.out.print("New Genre: ");
                    String genre = sc.nextLine();
                    if (genre.trim().isEmpty()) {
                        System.out.println("Invalid genre!");
                        return;
                    }
                    m.setGenre(genre);
                    break;

                case 3:
                    System.out.print("New Duration: ");
                    int duration = sc.nextInt();
                    if (duration <= 0) {
                        System.out.println("Invalid duration!");
                        return;
                    }
                    m.setDuration(duration);
                    break;

                case 4:
                    System.out.print("New Price: ");
                    int price = sc.nextInt();
                    if (price <= 0) {
                        System.out.println("Invalid price!");
                        return;
                    }
                    m.setPrice(price);
                    break;

                default:
                    System.out.println("Invalid choice!");
                    return;
            }

            System.out.println("Movie updated successfully!");

        } catch (MovieNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    // MEMBER 1: Manually implemented Bubble Sort
    static void sortMovies() {
        for (int i = 0; i < movies.size() - 1; i++) {
            for (int j = 0; j < movies.size() - i - 1; j++) {

                if (movies.get(j).getPrice() >
                        movies.get(j + 1).getPrice()) {

                    Movie temp = movies.get(j);
                    movies.set(j, movies.get(j + 1));
                    movies.set(j + 1, temp);
                }
            }
        }

        System.out.println("\nMovies sorted by ticket price:");
        displayMovies();
    }

    // Main menu
    public static void main(String[] args) {
        addSampleMovies();

        // Demonstrates runtime polymorphism
        User customer = new Customer("Guest");
        User admin = new Admin("Cinema Admin");

        System.out.println("Welcome to CineBook!");
        System.out.println("Customer Role: " + customer.getRole());
        System.out.println("Admin Role: " + admin.getRole());

        int choice;

        do {
            System.out.println("\n========== CINEBOOK ==========");
            System.out.println("1. View Movies");
            System.out.println("2. Book Tickets");
            System.out.println("3. View Bookings");
            System.out.println("4. Cancel Booking");
            System.out.println("5. Revenue Report");
            System.out.println("6. Add Movie");
            System.out.println("7. Remove Movie");
            System.out.println("8. Search Movie");
            System.out.println("9. Update Movie");
            System.out.println("10. Sort Movies by Price");
            System.out.println("11. View Movie Details");
            System.out.println("12. Exit");

            System.out.print("\nEnter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1: displayMovies(); break;
                case 2: bookTickets(); break;
                case 3: viewBookings(); break;
                case 4: cancelBooking(); break;
                case 5: revenueReport(); break;
                case 6: addMovie(); break;
                case 7: removeMovie(); break;
                case 8: searchMovie(); break;
                case 9: updateMovie(); break;
                case 10: sortMovies(); break;
                case 11:
                    try {
                        System.out.print("Enter Movie ID: ");
                        findMovie(sc.nextInt()).display();
                    } catch (MovieNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 12:
                    System.out.println("Thank you for using CineBook!");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 12);

        sc.close();
    }
}
