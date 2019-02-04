package com.scu.coen383.team2.ticketseller;

import java.util.Random;

public class SellerL extends Seller {
    private Object lock;
    public SellerL(Seat[][] s, String sellerID, Object lk, Random r) {
        super(s, r.nextInt(4) + 4, sellerID, lk, System.currentTimeMillis());
        lock = lk;

    }

    public void sell() {

    }

}
