
import java.util.ArrayList;

public class Booking {

    private int bookingId;
    private int movieId;

    private String customer;
    private String movieName;
    private String date;
    private String time;

    private ArrayList<Integer> seats;
    private double total;

    // Constructor to store booking information
    public Booking(int bookingId, String customer, Movie movie,
                   String date, String time,
                   ArrayList<Integer> seats) {

        this.bookingId = bookingId;
        this.customer = customer;
        this.movieId = movie.getId();
        this.movieName = movie.getName();
        this.date = date;
        this.time = time;

        this.seats = new ArrayList<>(seats);
        this.total = movie.getPrice() * seats.size();
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public ArrayList<Integer> getSeats() {
        return new ArrayList<>(seats);
    }

    public double getTotal() {
        return total;
    }

    // Displays complete booking details
    public void display() {

        System.out.println("\nBooking ID: " + bookingId);
        System.out.println("Customer: " + customer);
        System.out.println("Movie: " + movieName);
        System.out.println("Date: " + date);
        System.out.println("Time: " + time);
        System.out.println("Seats: " + seats);
        System.out.println("Total: Rs." + total);
    }
}
