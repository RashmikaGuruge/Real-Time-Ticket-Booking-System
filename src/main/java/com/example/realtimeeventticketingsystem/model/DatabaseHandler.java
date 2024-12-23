package com.example.realtimeeventticketingsystem.model;

import com.example.realtimeeventticketingsystem.config.DatabaseConfig;
import com.example.realtimeeventticketingsystem.util.SecurityUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {

    private static int currentVendorId;

    public static void setupTables() {
        try (Connection conn = DatabaseConfig.connect(); Statement stmt = conn.createStatement()) {

            String userTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE," +
                    "password TEXT," +
                    "email TEXT," +
                    "role TEXT)";

            String eventTable = "CREATE TABLE IF NOT EXISTS events (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "eventName TEXT," +
                    "eventDate TEXT," +
                    "location TEXT," +
                    "totalTickets INT," +
                    "ticketPrice DOUBLE," +
                    "vendorId INT)";

            String bookingTable = "CREATE TABLE IF NOT EXISTS bookings (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "eventId INT," +
                    "customerId INT," +
                    "quantity INT)";


            stmt.execute(userTable);
            stmt.execute(eventTable);
            stmt.execute(bookingTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Registers a new user in the database
    public static void registerUser(User user) {
        String sql = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getRole());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Authenticates a user with username and password
    public static User authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseConfig.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");
                String inputHashedPassword = SecurityUtil.hashPassword(password);

                if (storedHashedPassword.equals(inputHashedPassword)) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email"),
                            rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Adds a new event to the database
    public static boolean addEvent(Event event) {
        String sql = "INSERT INTO events (eventName, eventDate, location, totalTickets, ticketPrice, vendorId) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, event.getEventName());
            pstmt.setString(2, event.getEventDate());
            pstmt.setString(3, event.getLocation());
            pstmt.setInt(4, event.getTotalTickets());
            pstmt.setDouble(5, event.getTicketPrice());
            pstmt.setInt(6, event.getVendorId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Retrieves all events for display on the customer home screen
    public static List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events";
        try (Connection conn = DatabaseConfig.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                events.add(new Event(
                        rs.getInt("id"),
                        rs.getString("eventName"),
                        rs.getString("eventDate"),
                        rs.getString("location"),
                        rs.getInt("totalTickets"),
                        rs.getDouble("ticketPrice"),
                        rs.getInt("vendorId")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }


    // Retrieves all events created by a specific vendor for the vendor home screen
    public static List<Event> getEventsByVendor(int vendorId) {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events WHERE vendorId = ?";
        try (Connection conn = DatabaseConfig.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vendorId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("Event found: " + rs.getString("eventName"));
                events.add(new Event(
                        rs.getInt("id"),
                        rs.getString("eventName"),
                        rs.getString("eventDate"),
                        rs.getString("location"),
                        rs.getInt("totalTickets"),
                        rs.getDouble("ticketPrice"),
                        vendorId
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (events.isEmpty()) {
            System.out.println("No events found for vendor ID: " + vendorId);
        }
        return events;
    }




    // Books tickets for a customer and updates the total ticket count
    public static boolean bookTickets(int eventId, int customerId, int quantity) {
        Connection conn = null;
        String bookingSql = "INSERT INTO bookings (eventId, customerId, quantity) VALUES (?, ?, ?)";
        String updateEventSql = "UPDATE events SET totalTickets = totalTickets - ? WHERE id = ? AND totalTickets >= ?";

        try {
            conn = DatabaseConfig.connect();
            if (conn == null) {
                throw new SQLException("Failed to establish database connection.");
            }

            conn.setAutoCommit(false);

            try (PreparedStatement bookingStmt = conn.prepareStatement(bookingSql)) {
                bookingStmt.setInt(1, eventId);
                bookingStmt.setInt(2, customerId);
                bookingStmt.setInt(3, quantity);
                bookingStmt.executeUpdate();
            }

            try (PreparedStatement updateEventStmt = conn.prepareStatement(updateEventSql)) {
                updateEventStmt.setInt(1, quantity);
                updateEventStmt.setInt(2, eventId);
                updateEventStmt.setInt(3, quantity);
                int rowsAffected = updateEventStmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Not enough tickets available for booking.");
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Retrieves the current user's profile information
    public static User getCurrentUser(int userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseConfig.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("User found: " + rs.getString("username"));
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("No user found with ID: " + userId);
        return null;
    }

    // Retrieves the ID of the currently logged-in vendor
    public static int getCurrentVendorId() {
        if (currentVendorId == 0) {
            throw new IllegalStateException("No vendor is currently logged in.");
        }
        return currentVendorId;
    }

    // This method could be called from the authenticateUser method to set the vendor ID
    public static void setCurrentVendorId(int vendorId) {
        currentVendorId = vendorId;
    }

    public static Event getEventById(int eventId) {
        String sql = "SELECT * FROM events WHERE id = ?";
        try (Connection conn = DatabaseConfig.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Event(
                        rs.getInt("id"),
                        rs.getString("eventName"),
                        rs.getString("eventDate"),
                        rs.getString("location"),
                        rs.getInt("totalTickets"),
                        rs.getDouble("ticketPrice"),
                        rs.getInt("vendorId")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
