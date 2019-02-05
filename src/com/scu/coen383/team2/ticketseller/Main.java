package com.scu.coen383.team2.ticketseller;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.ArrayList;

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
        int[] totalSold = new int[1];
        int[] soldSeatsEachRow = new int [maxRows];
        Generator.generateSeats(seating, maxCols, maxCols);


        final CyclicBarrier gate = new CyclicBarrier(11);
        Seller[] sellers = new Seller[10];
        // initialize 10 sellers
        for (int numSeller = 0; numSeller < 10; numSeller++)
        {
            if (numSeller == 0)
                sellers[numSeller] = new SellerH(seating, soldSeatsEachRow, totalSold, "H" + (numSeller + 1), lock, random, gate);
            else if (numSeller >= 1 && numSeller < 4)
                sellers[numSeller] = new SellerM(seating, soldSeatsEachRow, totalSold, "M" + (numSeller), lock, random, gate);
            else if (numSeller >= 4 && numSeller < 10)
                sellers[numSeller] = new SellerL(seating, soldSeatsEachRow, totalSold, "L" + (numSeller - 3), lock, random, gate);
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

        //start all the thread at the same time
        try {
            gate.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }



        // wait for termination of each thread
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

        // print out statistics
        int pH = 0, nH = 0;
        int pM = 0, nM = 0;
        int pL = 0, nL = 0;

        for (int numSeller = 0; numSeller < 10; numSeller++)
        {
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
