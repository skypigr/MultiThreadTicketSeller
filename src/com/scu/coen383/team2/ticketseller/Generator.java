package com.scu.coen383.team2.ticketseller;

import java.util.Arrays;
import java.util.Random;

public class Generator {
    //Generate customers
    public void generateCustomers(Seller[] sellers, int customerCnt, Random generator) {
        for (int i = 0; i < sellers.length; i++) {
            for (int j = 0; j < customerCnt; j++) {
                int arrivalTime = generator.nextInt(60);
                Customer c = new Customer(i, arrivalTime);
                sellers[i].addCustomer(c);
            }
            sellers[i].sortQueue();
        }
    }
    //Generate seats matrix
    public void generateSeats(Seat[][] seating, int rowNum, int colNum) {
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                seating[i][j] = new Seat(i * colNum + j + 1);
            }
        }
    }
}
