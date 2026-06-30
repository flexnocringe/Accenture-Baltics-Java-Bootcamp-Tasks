package org.example.payment;

import org.example.model.Order;
import org.example.model.PaymentResult;

public class PaymentProcessor {
    public PaymentResult process(Order order, PaymentMethod paymentMethod) {
        // TODO: prevent paying already paid orders
        // TODO: prevent paying empty orders
        PaymentResult result;
        if (!order.getItems().isEmpty() || !order.isPaid()) {
            result = paymentMethod.pay(order.calculateTotal());
        } else {
            result = new PaymentResult(false, "Order is either empty or already paid.");
        }
        if (result.isSuccessful()) {
            order.markAsPaid();
        }

        return result;
    }
}
