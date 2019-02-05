package com.scu.coen383.team2.ticketseller;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class SellerL extends Seller {
    private Object lock;
    private  CyclicBarrier gate;
    public SellerL(Seat[][] s, int[] soldSeatsEachRow, int totalSold, String sellerID, Object lk, Random r, CyclicBarrier gate) {
        super(s, soldSeatsEachRow,  totalSold,r.nextInt(4) + 4, sellerID, lk, System.currentTimeMillis());
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

        while (!customers.isEmpty()) {
            Customer customer;
            if (customers.isEmpty() || 100 <= totalSold) return;
            // customer ready in the queue
            update();
            if(currentTime <= 59)
                customer = customers.peek();
            else
                return;

            // find seat
            Seat seat = null;

            synchronized(lock) {

                update();
                if(currentTime  >= (customer.getArrivalTime())){
                    find_seat:
                    for (int i = seating.length-1; i >= 0; i--) {
                        // assign seat
                        // seat number = (Row x 10) + (Col + 1)
                        if (soldSeatsEachRow[i] <= 9){

                            int seatNum = (i*10)+soldSeatsEachRow[i]+1;
                            seat = new Seat(seatNum);
                            super.assignSeat(customer, seat, i, soldSeatsEachRow[i]);
                            //update();
                            soldSeatsEachRow[i] ++;
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

        }
    }

}
