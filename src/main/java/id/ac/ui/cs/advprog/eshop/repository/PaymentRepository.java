package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class PaymentRepository {
    private final List<Payment> paymentData = new ArrayList<>();

    public Payment save(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < paymentData.size(); i++) {
            Payment savedPayment = paymentData.get(i);
            if (Objects.equals(savedPayment.getId(), payment.getId())) {
                paymentData.set(i, payment);
                return payment;
            }
        }

        paymentData.add(payment);
        return payment;
    }

    public Payment findById(String paymentId) {
        if (paymentId == null) {
            return null;
        }

        for (Payment payment : paymentData) {
            if (paymentId.equals(payment.getId())) {
                return payment;
            }
        }
        return null;
    }

    public List<Payment> findAll() {
        return new ArrayList<>(paymentData);
    }
}
