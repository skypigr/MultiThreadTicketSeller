package com.scu.coen383.team2.ticketseller;

public class Seat {

    private int seatNum;
    private Customer customer;

    public Seat(int seatNum) {
        this.seatNum = seatNum;
        customer = null;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean isSeatEmpty() {
        return customer == null;
    }
}