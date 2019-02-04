package com.scu.coen383.team2.ticketseller;

import java.util.Random;

public class CustomerGenerator {
    public static void generateJobs(Seller[] sellers, int customerCnt, Random generator) {

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
}
