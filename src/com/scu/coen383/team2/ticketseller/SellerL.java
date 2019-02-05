package com.scu.coen383.team2.ticketseller;

import java.util.Random;

public class SellerL extends Seller {
    private Object lock;
    public SellerL(Seat[][] s, String sellerID, Object lk, Random r) {
        super(s, r.nextInt(4) + 4, sellerID, lk, System.currentTimeMillis());
        lock = lk;

    }

    public void sell() {
        while (!customers.isEmpty()) {
            Customer customer;
            if (customers.isEmpty()) return;
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
                        for (int j = 0; j < seating[0].length; j++) {
                            if (seating[i][j].isSeatEmpty()) {
                                // assign seat
                                // seat number = (Row x 10) + (Col + 1)
                                int seatNum = (i*10)+j+1;
                                seat = new Seat(seatNum);
                                super.assignSeat(customer, seat, i, j);
                                //update();
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
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    }

}
