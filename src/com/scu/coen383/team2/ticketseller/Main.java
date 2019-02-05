package com.scu.coen383.team2.ticketseller;

import java.util.Random;
import java.util.Scanner;

public class Main {
    static Random random = new Random(1234);

    public static void main(String[] args) {
        System.out.println("Hello World");

        int customerCnt = 0;
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter the number of customers per seller: ");

        if(userInput.hasNextInt())
            customerCnt = userInput.nextInt();
        else
            System.out.println("Please restart and enter an integer.");

        final Object lock = new Object();


        int maxRows = 10;
        int maxCols = 10;
        Seat[][] seating = new Seat[maxRows][maxCols];
        Generator.generateSeats(seating, maxCols, maxCols);

        Seller[] sellers = new Seller[10];

        // initialize 10 sellers
        for (int numSeller = 0; numSeller < 10; numSeller++)
        {
            if (numSeller == 0)
                sellers[numSeller] = new SellerH(seating, "H" + (numSeller + 1), lock, random);
            else if (numSeller >= 1 && numSeller < 4)
                sellers[numSeller] = new SellerM(seating, "M" + (numSeller), lock, random);
            else if (numSeller >= 4 && numSeller < 10)
                sellers[numSeller] = new SellerL(seating, "L" + (numSeller - 3), lock, random);
        }

        // generate a customer queue for each seller
        Generator.generateCustomers(sellers, customerCnt, random);

        // print all customer, for test
        for (int i = 0; i < sellers.length; i++) {
            System.out.format("%4s: ", sellers[i].sellerID);
            for (Customer c: sellers[i].customers ) {
                System.out.format("%-3d ", c.getArrivalTime());
            }
            System.out.println("\n");

        }


        // create a thread for each seller
        Thread[] threads = new Thread[sellers.length];

        // start all thread at same time
        // TODO: make sure all thread start at the same time
        for(int numSellers = 0; numSellers < sellers.length; numSellers++)
        {
            threads[numSellers] = new Thread(sellers[numSellers]);
            threads[numSellers].start();

        }

        // wait for termination of each thread
        for (int i = 0; i < threads.length ; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


//         print all customer, for test
        for (int i = 0; i < sellers.length; i++) {
            System.out.format("%4s: ", sellers[i].sellerID);
            for (Customer c: sellers[i].customers ) {
                System.out.format("%-3d ", c.getArrivalTime());
            }
            System.out.println("\n");

        }

    }

    public static Seat[][] createSeating(int maxRows, int maxCols)
    {
        //create 10x10 seating and label with seat numbers
        Seat[][] seating = new Seat[maxRows][maxCols];
        int numSeat = 1;
        for (int row = 0; row < maxRows; row++)
        {
            for (int column = 0; column < maxCols; column++)
            {
                seating[row][column] = new Seat(numSeat);
                numSeat++;
            }
        }
        return seating;
    }
}
