package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentTest {

    @Test
    void testCreatePaymentStoresAllFields() {
        Order order = createOrder();
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment(
                "payment-1",
                order,
                "Voucher Code",
                "SUCCESS",
                paymentData
        );

        assertEquals("payment-1", payment.getId());
        assertEquals(order, payment.getOrder());
        assertEquals("Voucher Code", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertNotNull(payment.getPaymentData());
        assertEquals("ESHOP1234ABC5678", payment.getPaymentData().get("voucherCode"));
    }

    @Test
    void testSetStatusUpdatesStatus() {
        Payment payment = new Payment(
                "payment-2",
                createOrder(),
                "Cash on Delivery",
                "PENDING",
                Map.of("address", "Jl. Merdeka", "deliveryFee", "10000")
        );

        payment.setStatus("REJECTED");
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentWithNullPaymentDataCreatesEmptyMap() {
        Payment payment = new Payment(
                "payment-3",
                createOrder(),
                "Voucher Code",
                "REJECTED",
                null
        );

        assertNotNull(payment.getPaymentData());
        assertTrue(payment.getPaymentData().isEmpty());
    }

    private Order createOrder() {
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(1);

        return new Order(
                "order-1",
                List.of(product),
                1708560000L,
                "Safira Sudrajat"
        );
    }
}
