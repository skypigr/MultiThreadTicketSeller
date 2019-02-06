package com.scu.coen383.team2.ticketseller;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public abstract class Seller implements Runnable {
    protected long pastTime;
    protected long currentTime;
    protected Seat[][] seating;
    protected int[] soldSeatsEachRow;
    protected int[] totalSold;
    private Object lock;
    Queue<Customer> customers;
    protected String sellerID;
    //serviceTime is the time needed to purchase a ticket
    protected int serviceTime;
    protected int ticketNum = 1;
    protected int time = 0;

    public Seller(Seat[][] seating, int[] soldSeatsEachRow, int[]  totalSold, int serviceTime, String sellerID, Object lock, long current_time) {
        customers = new LinkedList<>();
        this.soldSeatsEachRow = soldSeatsEachRow;
        this.serviceTime = serviceTime;
        this.seating = seating;
        this.totalSold =  totalSold;
        this.sellerID = sellerID;
        this.lock = lock;
        this.pastTime = current_time;
    }

    protected void calTime(Customer customer) {
        time = (int) (currentTime + serviceTime);
        customer.setFinishTime(time);
    }

    protected void printMsg(Customer customer, Seat seat) {
        int hour = customer.getFinishTime() / 60;
        int min = customer.getFinishTime() % 60;

        System.out.println("");
        String time = "";
        if (min < 10) {
            time = hour + ":0" + min;
        } else time = hour + ":" + min;
        if (seat == null) System.out.println(time + "  " + sellerID + " - The concert is sold out.");
        else System.out.println("#" + time + "  " + sellerID + " - Success! Your seat is " + seat.getSeatNum());

        printSeating(this.seating, 10, 10);
    }

    protected void assignSeat(Customer customer, Seat seat, int i, int j) {
        if (ticketNum < 10)
            customer.setTicket(sellerID + "0" + ticketNum);
        else
            customer.setTicket(sellerID + ticketNum);

        calTime(customer);
        ticketNum++;
        seat.setCustomer(customer);
        seating[i][j] = seat;
    }

    protected void update() {
        currentTime = System.currentTimeMillis() - this.pastTime;
        if (currentTime < 1000)
            currentTime = 0;
        else
            currentTime /= 1000;
    }

    public void addCustomer(Customer c) {
        customers.add(c);
    }

    public abstract void sell();

    @Override
    public void run() {
        sell();
    }

    public static void printSeating(Seat[][] seating, int rowNum, int colNum) {
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                if (seating[i][j].isSeatEmpty())
                    System.out.printf("%s ", "----");
                else
                    System.out.printf("%s ", seating[i][j].getCustomer().getTicket());
            }
            System.out.println();
        }
    }


    public void sortQueue() {
        Customer[] array = customers.toArray(new Customer[customers.size()]);
        customers.clear();
        Arrays.sort(array);
        for (Customer c : array) {
            customers.add(c);
        }
    }
}
