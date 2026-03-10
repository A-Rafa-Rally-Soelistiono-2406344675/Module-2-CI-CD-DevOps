package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;

import java.util.List;
import java.util.Arrays;

@Getter
public class Order {
    String id;
    List<Product> products;
    Long orderTime;
    String author;

    String status;

    public Order(String id, List<Product> products, Long orderTime, String author) {
        this.id = id;
        this.orderTime = orderTime;
        this.author = author;
        this.status = OrderStatus.WAITING_PAYMENT.getValue();

        if (products.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            this.products = products;
        }
    }

    public Order(String id, List<Product> products, Long orderTime, String author, String status) {
        this(id, products, orderTime, author);
        this.setStatus(status);
    }

    public final void setStatus(String status) {
        if (OrderStatus.contains(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
