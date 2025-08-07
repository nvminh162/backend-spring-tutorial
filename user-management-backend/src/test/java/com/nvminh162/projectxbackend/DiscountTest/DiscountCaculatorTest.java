package com.nvminh162.projectxbackend.DiscountTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DiscountCaculatorTest {

    @Test
    public void testNoDiscount() {
        DiscountCaculator discountCaculator = new DiscountCaculator();
        // < 100
        double total = discountCaculator.calculateDiscount(50);
        assertEquals(0, total);
    }

    @Test
    public void test10Discount() {
        DiscountCaculator discountCaculator = new DiscountCaculator();
        // < 500
        double total = discountCaculator.calculateDiscount(150);
        assertEquals(15, total);

    }

    @Test
    public void test20Discount() {
        DiscountCaculator discountCaculator = new DiscountCaculator();
        // >~
        double total = discountCaculator.calculateDiscount(600);
        assertEquals(120, total);
    }
}
