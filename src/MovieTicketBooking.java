
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class MovieTicketBooking {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<Movie> movies = new ArrayList<>();
    static ArrayList<Booking> bookings = new ArrayList<>();

    // Stores booked seats for each movie, date and showtime
    static HashMap<String, HashSet<Integer>> occupiedSeats =
            new HashMap<>();

    static int nextBookingId = 101;

    // Adds some sample movies
    static void addSampleMovies() {

        movies.add(new Movie(1, "Interstellar", "Sci-Fi", 169, 180));
        movies.add(new Movie(2, "Inception", "Thriller", 148, 150));
        movies.add(new Movie(3, "Avatar", "Adventure", 192, 200));
        movies.add(new Movie(4, "Avengers", "Action", 181, 250));
    }

    // Searches for a movie using its ID
    static Movie findMovie(int id) throws MovieNotFoundException {

        for (Movie m : movies) {
            if (m.getId() == id) {
                return m;
            }
        }

        throw new MovieNotFoundException("Movie not found!");
    }

    // Displays all available movies
    static void displayMovies() {

        if (movies.isEmpty()) {
            System.out.println("No movies available!");
            return;
        }

        System.out.println("\nID | Name | Genre | Duration | Price");

        for (Movie m : movies) {
            m.display();
        }
    }

    // Displays movie showtimes
    static String getShowTime() {

        System.out.println("\nAvailable Showtimes");
        System.out.println("1. 10:00 AM");
        System.out.println("2. 2:00 PM");
        System.out.println("3. 6:00 PM");
        System.out.println("4. 9:00 PM");

        System.out.print("Select Show: ");

        switch (sc.nextInt()) {

            case 1:
                return "10:00 AM";

            case 2:
                return "2:00 PM";

            case 3:
                return "6:00 PM";

            case 4:
                return "9:00 PM";

            default:
                return null;
        }
    }

    // Checks and selects available seats
    static ArrayList<Integer> selectSeats(String key)
            throws SeatNotAvailableException {

        HashSet<Integer> booked = occupiedSeats.get(key);

        if (booked == null) {
            booked = new HashSet<>();
            occupiedSeats.put(key, booked);
        }

        ArrayList<Integer> selected = new ArrayList<>();
        HashSet<Integer> temporary = new HashSet<>();

        System.out.println("Available seats: 1 to 20");
        System.out.println("Booked seats: " + booked);

        System.out.print("Enter number of seats: ");
        int count = sc.nextInt();

        if (count <= 0 || count > 20) {
            throw new SeatNotAvailableException(
                    "Invalid seat quantity!");
        }

        for (int i = 0; i < count; i++) {

            System.out.print("Enter seat number: ");
            int seat = sc.nextInt();

            if (seat < 1 || seat > 20 ||
                    booked.contains(seat) ||
                    !temporary.add(seat)) {

                throw new SeatNotAvailableException(
                        "Seat " + seat + " is unavailable!");
            }

            selected.add(seat);
        }

        // Reserve seats after checking all selections
        booked.addAll(selected);

        return selected;
    }

    // Books movie tickets
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

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

            try {
                LocalDate selectedDate = LocalDate.parse(date, formatter);

                if (selectedDate.isBefore(LocalDate.now())) {
                System.out.println("Cannot book tickets for a past date!");
                return;
                }

            } catch (Exception e) {
                System.out.println("Invalid date!");
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

        } catch (MovieNotFoundException |
                 SeatNotAvailableException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    // Displays all bookings
    static void viewBookings() {

        if (bookings.isEmpty()) {
            System.out.println("No bookings available!");
            return;
        }

        for (Booking b : bookings) {
            b.display();
        }
    }

    // Cancels booking and releases seats
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

            if (found == null) {
                throw new BookingNotFoundException(
                        "Booking not found!");
            }

            String key = found.getMovieId() + "|" +
                    found.getDate() + "|" + found.getTime();

            HashSet<Integer> seats = occupiedSeats.get(key);

            if (seats != null) {
                seats.removeAll(found.getSeats());
            }

            bookings.remove(found);

            System.out.println("Booking cancelled successfully!");

        } catch (BookingNotFoundException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    // Calculates total revenue
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

    // Adds a new movie
    

static void addMovie() {

    System.out.print("Enter Movie ID: ");
    if (!sc.hasNextInt()) {
        System.out.println("Invalid Movie ID! Enter a number.");
        sc.nextLine();
        return;
    }
    int id = sc.nextInt();

    if (id <= 0) {
        System.out.println("Movie ID must be positive!");
        return;
    }

    for (Movie m : movies) {
        if (m.getId() == id) {
            System.out.println("Movie ID already exists!");
            return;
        }
    }

    sc.nextLine();

    System.out.print("Movie Name: ");
    String name = sc.nextLine().trim();

    if (name.isEmpty()) {
        System.out.println("Movie name cannot be empty!");
        return;
    }

    for (Movie m : movies) {
        if (m.getName().equalsIgnoreCase(name)) {
            System.out.println("Movie already exists!");
            return;
        }
    }

    System.out.print("Genre: ");
    String genre = sc.nextLine().trim();

    if (genre.isEmpty()) {
        System.out.println("Genre cannot be empty!");
        return;
    }

    System.out.print("Duration: ");
    if (!sc.hasNextInt()) {
        System.out.println("Invalid duration! Enter a number.");
        sc.nextLine();
        return;
    }
    int duration = sc.nextInt();

    System.out.print("Ticket Price: ");
    if (!sc.hasNextInt()) {
        System.out.println("Invalid ticket price! Enter a number.");
        sc.nextLine();
        return;
    }
    int price = sc.nextInt();

    if (duration <= 0 || price <= 0) {
        System.out.println("Duration and price must be positive!");
        return;
    }

    movies.add(new Movie(id, name, genre, duration, price));

    System.out.println("Movie added successfully!");
}



    // Removes a movie
    static void removeMovie() {

        try {

            System.out.print("Enter Movie ID: ");
            Movie m = findMovie(sc.nextInt());

            for (Booking b : bookings) {

                if (b.getMovieId() == m.getId()) {

                    System.out.println(
                            "Cannot remove a movie with active bookings!");
                    return;
                }
            }

            movies.remove(m);

            System.out.println("Movie removed successfully!");

        } catch (MovieNotFoundException e) {

            System.out.println(e.getMessage());
        }
    }

    // Searches movies by name or genre
    // Searches movies by name or genre
static void searchMovie() {

    sc.nextLine();

    System.out.print("Enter Movie Name or Genre: ");
    String search = sc.nextLine().trim().toLowerCase();

    if (search.isEmpty()) {
        System.out.println("Search cannot be empty!");
        return;
    }

    boolean found = false;

    for (Movie m : movies) {

        if (m.getName().toLowerCase().contains(search) ||
                m.getGenre().toLowerCase().contains(search)) {

            m.display();
            found = true;
        }
    }

    if (!found) {
        System.out.println("No matching movies found!");
    }
}

    // Updates movie information
    static void updateMovie() {

        try {

            System.out.print("Enter Movie ID: ");
            Movie m = findMovie(sc.nextInt());

            System.out.println("\n1. Update Name");
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

    // Manually implemented Bubble Sort
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

    // Main method
    
public static void main(String[] args) {

    addSampleMovies();

    User customer = new Customer("Guest");
    User admin = new Admin("Cinema Admin");

    System.out.println("\n===== MOVIE TICKET BOOKING SYSTEM =====");
    System.out.println("Customer Role: " + customer.getRole());
    System.out.println("Admin Role: " + admin.getRole());

    int choice = 0;

    do {

        System.out.println("\n========== MOVIE TICKET BOOKING SYSTEM ==========");
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

        String input = sc.next();

        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a number.");
            continue;
        }

        switch (choice) {

            case 1:
                displayMovies();
                break;

            case 2:
                bookTickets();
                break;

            case 3:
                viewBookings();
                break;

            case 4:
                cancelBooking();
                break;

            case 5:
                revenueReport();
                break;

            case 6:
                addMovie();
                break;

            case 7:
                removeMovie();
                break;

            case 8:
                searchMovie();
                break;

            case 9:
                updateMovie();
                break;

            case 10:
                sortMovies();
                break;

            case 11:
                try {
                    System.out.print("Enter Movie ID: ");
                    findMovie(sc.nextInt()).display();
                } catch (MovieNotFoundException e) {
                    System.out.println(e.getMessage());
                }
                break;

            case 12:
                System.out.println(
                    "Thank you for using Movie Ticket Booking System!");
                break;

            default:
                System.out.println("Invalid choice!");
        }

    } while (choice != 12);

    sc.close();
}

}
