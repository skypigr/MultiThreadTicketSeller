package com.scu.coen383.team2.ticketseller;

public class Customer implements Comparable<Customer> {
    private int _arrivalTime;
    private int _time; // finishTime
    private int _seatNum;
    private int _id;
    private String _ticket;


    public Customer(int id, int arrivalTime) {
        _id = id;
        _arrivalTime = arrivalTime;//random
        _seatNum = -1;
    }

    public int getTime()                { return _time; }
    public int getArrivalTime()         { return _arrivalTime;}
    public int getSeatNum()             { return _seatNum;}
    public int getId()                  { return _id;}
    public String getTicket()           { return _ticket; }

    public void setTime(int newTime)    {_time = newTime;}
    public void setTicket(String newTicket)    {_ticket = newTicket;}
    public void setSeatNum(int newNum) {_seatNum = newNum;}
    public boolean isSigned() { return _seatNum != -1;}


    @Override
    public int compareTo(Customer obj) {
        return Integer.compare(_arrivalTime, obj._arrivalTime);
    }
}
