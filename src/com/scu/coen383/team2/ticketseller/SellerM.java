package com.scu.coen383.team2.ticketseller;

public class SellerM extends Seller {
    private Object lock;
    public SellerM(Seat[][] s, String sellerID, Object lk) {
        super(s, r.nextInt(2) + 1, sellerID, lk, System.currentTimeMillis());
        lock = lk;
    }

    public void sell() {

    }

}
