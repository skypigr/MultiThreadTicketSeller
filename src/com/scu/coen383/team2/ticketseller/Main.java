package com.scu.coen383.team2.ticketseller;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Main {
    static Random random = new Random(1234);

    public static void main(String[] args) {
        System.out.println("Hello World");
        System.out.println("Please enter the number of customers per seller: ");
        Scanner userInput = new Scanner(System.in);
        int customerCnt = 0;
        if (userInput.hasNextInt()) {
            customerCnt = userInput.nextInt();
        } else {
            System.out.println("Customers count is needed, please enter an integer.");
        }

        Generator generator = new Generator();

        //Generate the 10*10 seating matrix
        int rowNum = 10;
        int colNum = 10;
        Seat[][] seating = new Seat[rowNum][colNum];
        generator.generateSeats(seating, rowNum, colNum);

        //Use soldSeatsEachRow to record how many seats are already sold in each row
        int[] soldSeatsEachRow = new int[rowNum];
        final Object lock = new Object();
        int[] totalSold = new int[1];

        final CyclicBarrier gate = new CyclicBarrier(11);
        Seller[] sellers = new Seller[10];
        //Initialize 10 sellers
        for (int i = 0; i < 10; i++) {
            if (i == 0) {
                sellers[i] = new SellerH(seating, soldSeatsEachRow, totalSold, "H" + (i + 1), lock, random, gate);
            } else if (i >= 1 && i < 4) {
                sellers[i] = new SellerM(seating, soldSeatsEachRow, totalSold, "M" + (i), lock, random, gate);
            } else {
                sellers[i] = new SellerL(seating, soldSeatsEachRow, totalSold, "L" + (i - 3), lock, random, gate);
            }
        }

        //Generate a customer queue for each seller
        generator.generateCustomers(sellers, customerCnt, random);

        //Print all customer, for test
        for (int i = 0; i < sellers.length; i++) {
            System.out.format("%4s: ", sellers[i].sellerID);
            for (Customer c: sellers[i].customers ) {
                System.out.format("%-3d ", c.getArrivalTime());
            }
            System.out.println("\n");
        }

        //Create a thread for each seller
        Thread[] threads = new Thread[sellers.length];

        //Start all thread at same time
        for(int numSellers = 0; numSellers < sellers.length; numSellers++) {
            threads[numSellers] = new Thread(sellers[numSellers]);
            threads[numSellers].start();
        }

        //Start all the thread at the same time
        try {
            gate.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        //Wait for termination of each thread
        for (int i = 0; i < threads.length ; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // print all customer, for test
        System.out.println("\nRemaining Customer\n");

//         print all customer, for test

        for (int i = 0; i < sellers.length; i++) {
            System.out.format("%4s: ", sellers[i].sellerID);
            for (Customer c: sellers[i].customers ) {
                System.out.format("%-3d ", c.getArrivalTime());
            }
            System.out.println("\n");

        }

        //Print out statistics， pH, pM and pL are the sold number, nH, nM, nL are the remaining number
        int pH = 0, nH = 0;
        int pM = 0, nM = 0;
        int pL = 0, nL = 0;

        for (int numSeller = 0; numSeller < 10; numSeller++) {
            if (numSeller == 0) {
                nH += sellers[numSeller].customers.size();
                pH += customerCnt - sellers[numSeller].customers.size();
            }
            else if (numSeller >= 1 && numSeller < 4) {
                nM += sellers[numSeller].customers.size();
                pM += customerCnt - sellers[numSeller].customers.size();
            }

            else if (numSeller >= 4 && numSeller < 10) {
                nL += sellers[numSeller].customers.size();
                pL += customerCnt - sellers[numSeller].customers.size();
            }
        }

        System.out.format("Ticket Sold:\nH:%3d, M:%3d, L:%3d\n", pH, pM, pL);
        System.out.format("Customer Turned Away:\nH:%3d, M:%3d, L:%3d\n", nH, nM, nL);
    }
}
