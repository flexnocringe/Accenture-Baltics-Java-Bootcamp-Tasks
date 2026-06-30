package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private final String customerName;
    private final List<OrderItem> items;
    private OrderStatus status;
    private Discount discount = new NoDiscount();

    public Order(Builder builder) {
        this.customerName = builder.customerName;
        this.items = builder.items;
        this.status = OrderStatus.NEW;
    }

    public void addItem(OrderItem item) {
        if (status == OrderStatus.NEW) {
            items.add(item);
        } else {
            System.out.println("Cannot add items to a paid / cancelled order.");
        }
    }

    public double calculateTotal() {
        // TODO: calculate total from all order items (including discounts)
        double sum = 0.0;
        for (OrderItem item : items) {
            sum += item.calculateTotal();
        }
        sum = discount.apply(sum);
        return sum;
    }

    public void markAsPaid() {
        // TODO: validate order is not empty
        if (!items.isEmpty()) {
            this.status = OrderStatus.PAID;
        } else {
            this.status = OrderStatus.CANCELLED;
        }

    }

    public void applyDiscount(Discount discount) {
        this.discount = discount;
    }

    public boolean isPaid() {
        return this.status == OrderStatus.PAID;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public String getCustomerName() {
        return customerName;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String customerName;
        private List<OrderItem> items = new ArrayList<>();

        public Builder customerName(String customerName) {
            this.customerName = customerName;
            return this;
        }

        public Builder addItem(OrderItem item) {
            this.items.add(item);
            return this;
        }

        public Order build() {
            // TODO: validate customerName
            if (customerName == null) {
                throw new IllegalStateException("Customer name is required");
            } else {
                return new Order(this);
            }
        }
    }
}
