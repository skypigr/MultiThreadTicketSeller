package com.scu.coen383.team2.ticketseller;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class SellerM extends Seller {
    private Object lock;
    private CyclicBarrier gate;
    public SellerM(Seat[][] s, int[] soldSeatsEachRow, int[] totalSold,String sellerID, Object lk, Random r, CyclicBarrier gate) {
        super(s, soldSeatsEachRow,  totalSold,r.nextInt(2) + 1, sellerID, lk, System.currentTimeMillis());
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
            if(customers.isEmpty() || 100 <= totalSold[0]){
                return;
            } else if (currentTime < customers.peek().getArrivalTime()){
                try {
                    Thread.sleep(1 * 1000);
                    update();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }


            update();
            if(currentTime <= 59) {
                customer = customers.peek();
            } else {
                return;
            }
            boolean flag = true;
            int counter = 1;

            Seat seat = null;

            synchronized(lock) {
                update();
                if(currentTime  >= (customer.getArrivalTime())){
                    find_seat:
                    for(int i = 5; i >= 0 && i < seating.length;) {
                        if (soldSeatsEachRow[i] <= 9){
                            // Assign seat to customer
                            // Seat number = (Row x 10) + (Col + 1)
                            int seatNum = (i * 10) + soldSeatsEachRow[i] + 1;
                            seat = new Seat(seatNum);
                            super.assignSeat(customer, seat, i, soldSeatsEachRow[i]);
                            //update();
                            soldSeatsEachRow[i] ++;
                            totalSold[0] ++;
                            printMsg(customer, seat);
                            customers.remove();
                            break find_seat;
                        }
                        if(flag == true){
                            i += counter;
                            flag = false;
                        }
                        else{
                            i -= counter;
                            flag = true;
                        }
                        counter++;
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
