package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    private static final String STATUS_SUCCESS = "SUCCESS";
    private static final String STATUS_REJECTED = "REJECTED";
    private static final String METHOD_VOUCHER_CODE = "VOUCHER_CODE";
    private static final String METHOD_CASH_ON_DELIVERY = "CASH_ON_DELIVERY";

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        if (order == null || method == null) {
            throw new IllegalArgumentException();
        }

        String status = resolveInitialStatus(method, paymentData);
        Payment payment = new Payment(
                UUID.randomUUID().toString(),
                order,
                method,
                status,
                paymentData
        );
        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        if (payment == null || status == null) {
            throw new IllegalArgumentException();
        }

        payment.setStatus(status);
        if (STATUS_SUCCESS.equals(status)) {
            payment.getOrder().setStatus(OrderStatus.SUCCESS.getValue());
        } else if (STATUS_REJECTED.equals(status)) {
            payment.getOrder().setStatus(OrderStatus.FAILED.getValue());
        }

        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    private String resolveInitialStatus(String method, Map<String, String> paymentData) {
        String normalizedMethod = normalizeMethod(method);
        if (METHOD_VOUCHER_CODE.equals(normalizedMethod)) {
            return isVoucherCodeValid(paymentData) ? STATUS_SUCCESS : STATUS_REJECTED;
        }

        if (METHOD_CASH_ON_DELIVERY.equals(normalizedMethod)) {
            return isCodDataValid(paymentData) ? STATUS_SUCCESS : STATUS_REJECTED;
        }

        throw new IllegalArgumentException();
    }

    private String normalizeMethod(String method) {
        return method.trim().toUpperCase().replace(' ', '_');
    }

    private boolean isVoucherCodeValid(Map<String, String> paymentData) {
        if (paymentData == null) {
            return false;
        }

        String voucherCode = paymentData.get("voucherCode");
        if (voucherCode == null || voucherCode.length() != 16 || !voucherCode.startsWith("ESHOP")) {
            return false;
        }

        int digitCount = 0;
        for (char ch : voucherCode.toCharArray()) {
            if (Character.isDigit(ch)) {
                digitCount++;
            }
        }
        return digitCount == 8;
    }

    private boolean isCodDataValid(Map<String, String> paymentData) {
        if (paymentData == null) {
            return false;
        }

        String address = paymentData.get("address");
        String deliveryFee = paymentData.get("deliveryFee");
        return address != null && !address.isBlank() && deliveryFee != null && !deliveryFee.isBlank();
    }
}
