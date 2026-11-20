#  Movie Ticket Booking System

## Project Description

A console-based Movie Ticket Booking System developed using Core Java. The application allows customers to browse movies, select showtimes and seats, book tickets, and manage reservations. It also provides administrative features for managing movies and generating revenue reports.

## Features

### Customer Features
- View available movies
- Search movies by name or genre
- View detailed movie information
- Select showtimes
- Select available seats
- Book movie tickets
- View booking details
- Cancel bookings

### Admin Features
- Add new movies
- Remove existing movies
- Update movie details
- Sort movies by ticket price
- Generate revenue reports
- View total bookings and tickets sold

## Technologies Used

- **Programming Language:** Java
- **IDE:** Any Java-compatible IDE
- **Collections:** ArrayList, HashMap, HashSet
- **Concepts:** OOP, Exception Handling, DSA

## Object-Oriented Programming Concepts

| Concept | Implementation |
|---|---|
| Encapsulation | Private data members with getters and setters |
| Inheritance | Customer and Admin inherit from User |
| Polymorphism | Runtime method overriding |
| Abstraction | Abstract User class |

## Data Structures and Algorithms

- **ArrayList:** Stores movies and bookings.
- **HashMap:** Maintains occupied seats for movie showtimes.
- **HashSet:** Prevents duplicate seat selection.
- **Bubble Sort:** Sorts movies based on ticket price.

## Exception Handling

Custom exceptions implemented:
- MovieNotFoundException
- SeatNotAvailableException
- BookingNotFoundException

These exceptions handle invalid movie searches, unavailable seats and missing bookings.

## Project Structure

```text
MovieTicketBookingSystem/
│
├── src/
│   ├── MovieTicketBooking.java
│   ├── Movie.java
│   ├── Booking.java
│   ├── User.java
│   ├── Customer.java
│   ├── Admin.java
│   ├── MovieNotFoundException.java
│   ├── SeatNotAvailableException.java
│   └── BookingNotFoundException.java
│
├── out/
├── README.md
└── .gitignore
```

## How to Run the Project

**Step 1: Compile the Java files**

```bash
javac -d out src/*.java
```

**Step 2: Run the application**

```bash
java -cp out MovieTicketBooking
```

## Application Type

Java Console-Based Application

## Project Objective

To apply Core Java programming, Object-Oriented Programming principles, Java Collections, exception handling and Data Structures and Algorithms concepts by developing a practical movie ticket reservation system.
