package com.example.realtimeeventticketingsystem.model;

public class Event {
    private int id;
    private String eventName;
    private String eventDate;
    private String location;
    private int totalTickets;
    private double ticketPrice;
    private int vendorId;

    public Event(String eventName, String eventDate, String location, int totalTickets, double ticketPrice, int vendorId) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.location = location;
        this.totalTickets = totalTickets;
        this.ticketPrice = ticketPrice;
        this.vendorId = vendorId;
    }

    public Event(int id, String eventName, String eventDate, String location, int totalTickets, double ticketPrice, int vendorId) {
        this.id = id;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.location = location;
        this.totalTickets = totalTickets;
        this.ticketPrice = ticketPrice;
        this.vendorId = vendorId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getTotalTickets() { return totalTickets; }
    public void setTotalTickets(int totalTickets) { this.totalTickets = totalTickets; }

    public double getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(double ticketPrice) { this.ticketPrice = ticketPrice; }

    public int getVendorId() { return vendorId; }
    public void setVendorId(int vendorId) { this.vendorId = vendorId; }
}
