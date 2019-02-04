package com.scu.coen383.team2.ticketseller;

import java.util.Scanner;

public class Main {
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
        Seat[][] seating = createSeating(maxRows, maxCols);

        Seller[] sellers = new Seller[10];
        for (int numSeller = 0; numSeller < 10; numSeller++)
        {
            if (numSeller == 0)
                sellers[numSeller] = new SellerH(seating, "H" + (numSeller + 1), lock);
            else if (numSeller >= 1 && numSeller < 4)
                sellers[numSeller] = new SellerM(seating, "M" + (numSeller), lock);
            else if (numSeller >= 4 && numSeller < 10)
                sellers[numSeller] = new SellerL(seating, "L" + (numSeller - 3), lock);
        }

        addNewCustomers(sellers, customerCnt);



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

    public static Seller[] addNewCustomers(Seller[] allSellers, int numAdd)
    {
        for (int numSeller = 0; numSeller < allSellers.length; numSeller++)
        {
            for (int count = 0; count < numAdd; count++)
            {
                Customer c = new Customer(numSeller);
                allSellers[numSeller].addCustomer(c);
            }
            allSellers[numSeller].sortQueue();
        }
        return allSellers;
    }

}
