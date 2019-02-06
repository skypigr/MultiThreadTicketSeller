package com.scu.coen383.team2.ticketseller;

public class Customer implements Comparable<Customer> {
    private int _arrivalTime;
    private int _finishTime;
    private String _ticket;

    public Customer(int id, int arrivalTime) {
        //The arrival time is created random when initialize the customer
        _arrivalTime = arrivalTime;
    }

    public int getFinishTime()                { return _finishTime; }
    public int getArrivalTime()         { return _arrivalTime;}
    public String getTicket()           { return _ticket; }

    public void setFinishTime(int newTime)    {_finishTime = newTime;}
    public void setTicket(String ticket)    {_ticket = ticket;}


    @Override
    public int compareTo(Customer obj) {
        return Integer.compare(_arrivalTime, obj._arrivalTime);
    }
}
