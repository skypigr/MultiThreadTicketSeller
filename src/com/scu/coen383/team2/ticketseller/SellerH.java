package com.scu.coen383.team2.ticketseller;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class SellerH extends Seller {


    private Object lock;
    private CyclicBarrier gate;
    public SellerH(Seat[][] s, int[] soldSeatsEachRow, int[] totalSold, String sellerID, Object lk, Random r, CyclicBarrier gate) {
        super(s, soldSeatsEachRow,  totalSold, r.nextInt(2) + 1, sellerID, lk, System.currentTimeMillis());
        lock = lk;
        this.gate = gate;
    }

    public void sell() {
        try {
            gate.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }


        while (!customers.isEmpty() && totalSold[0] < 100) {
            Customer customer;
            if (currentTime < customers.peek().getArrivalTime()){
                try {
                    Thread.sleep(1 * 1000);
                    update();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            update();

            if(currentTime <= 59)
                customer = customers.peek();
            else
                return;
            Seat seat = null;

            synchronized(lock) {

                update();
                if(currentTime  >= (customer.getArrivalTime())){
                    find_seat:
                    //TODO: use seatIndex for each row to avoid loop the whole Seat Table
                    for (int i = 0; i < seating.length; i++) {
                        if (soldSeatsEachRow[i] <= 9) {
                            // Seat number = (Row x 10) + (Col + 1)
                            int seatNum = (i*10) + soldSeatsEachRow[i] + 1;
                            seat = new Seat(seatNum);
                            assignSeat(customer, seat, i, soldSeatsEachRow[i]);
                            soldSeatsEachRow[i] ++;
                            totalSold[0] ++;
                            printMsg(customer, seat);
                            customers.remove();
                            break find_seat;

                        }
                    }
                }
            }
            if(seat != null){
                try {
                    Thread.sleep(serviceTime * 1000);
                    update();
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }

            update();

        }
    }

}
