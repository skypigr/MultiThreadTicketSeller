package com.scu.coen383.team2.ticketseller;

import java.util.Random;

public class Generator {
    public static void generateCustomers(Seller[] sellers, int customerCnt, Random generator) {

        for (int numSeller = 0; numSeller < sellers.length; numSeller++)
        {
            for (int count = 0; count < customerCnt; count++)
            {
                int arrivalTime = generator.nextInt(60);
                Customer c = new Customer(numSeller, arrivalTime);
                sellers[numSeller].addCustomer(c);
            }
            sellers[numSeller].sortQueue();
        }
    }

    public static void generateSeats(Seat[][] seating, int maxRows, int maxCols) {
        //create 10x10 seating and label with seat numbers

        int numSeat = 1;
        for (int row = 0; row < maxRows; row++)
        {
            for (int column = 0; column < maxCols; column++)
            {
                seating[row][column] = new Seat(numSeat);
                numSeat++;
            }
        }
    }
}
