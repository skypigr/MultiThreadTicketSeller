package com.scu.coen383.team2.ticketseller;

import java.util.Random;

public class SellerM extends Seller {
    private Object lock;
    public SellerM(Seat[][] s, String sellerID, Object lk, Random r) {
        super(s, r.nextInt(2) + 1, sellerID, lk, System.currentTimeMillis());
        lock = lk;
    }

    public void sell() {
        while (!customers.isEmpty()) {
            Customer customer;
            if (customers.isEmpty()) return;


            update();
            if(currentTime <= 59)
                customer = customers.peek();
            else
                return;
            boolean flag = true;
            int counter = 1;

            Seat seat = null;

            synchronized(lock) {

                update();

                if(currentTime  >= (customer.getArrivalTime())){
                    find_seat:
                    for(int i = 5; i >= 0 && i < seating.length;) {
                        for (int j = 0; j < seating[0].length; j++) {
                            if (seating[i][j].isSeatEmpty()) {
                                // Assign seat to customer
                                // Seat number = (Row x 10) + (Col + 1)
                                int seatNum = (i*10)+j+1;
                                seat = new Seat(seatNum);
                                super.assignSeat(customer, seat, i, j);
                                //update();
                                printMsg(customer, seat);
                                customers.remove();
                                break find_seat;
                            }
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


        }
    }

}
