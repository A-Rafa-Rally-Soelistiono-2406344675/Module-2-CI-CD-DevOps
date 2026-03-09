package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Payment {
    private final String id;
    private final Order order;
    private final String method;
    @Setter
    private String status;
    private final Map<String, String> paymentData;

    public Payment(String id,
                   Order order,
                   String method,
                   String status,
                   Map<String, String> paymentData) {
        this.id = id;
        this.order = order;
        this.method = method;
        this.status = status;
        this.paymentData = new HashMap<>(paymentData);
    }
}
