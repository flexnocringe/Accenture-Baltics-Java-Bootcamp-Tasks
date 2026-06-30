package org.example.model;

public class FixedAmountDiscount extends Discount {
    // TODO
    // 'apply' formula: Math.max(0,originalAmount - amount)
    private int amount = 5;

    protected FixedAmountDiscount(String code) {
        super(code);
    }

    @Override
    public double apply(double originalAmount) {
        return Math.max(0, originalAmount - amount);
    }

}
