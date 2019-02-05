package com.scu.coen383.team2.ticketseller;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class SellerH extends Seller {


    private Object lock;
    private CyclicBarrier gate;
    public SellerH(Seat[][] s, String sellerID, Object lk, Random r, CyclicBarrier gate) {
        super(s, r.nextInt(2) + 1, sellerID, lk, System.currentTimeMillis());
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

            Customer customer = null;
            if (customers.isEmpty()) return;
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
                        for (int j = 0; j < seating[0].length; j++) {
                            if (seating[i][j].isSeatEmpty()) {

                                // Seat number = (Row x 10) + (Col + 1)
                                int seatNum = (i*10)+j+1;
                                seat = new Seat(seatNum);
                                assignSeat(customer, seat, i, j);
                                printMsg(customer, seat);
                                customers.remove();

                                break find_seat;
                            }
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
