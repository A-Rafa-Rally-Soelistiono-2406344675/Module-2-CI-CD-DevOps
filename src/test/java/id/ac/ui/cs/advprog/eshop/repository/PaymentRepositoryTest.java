package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class PaymentRepositoryTest {

    private PaymentRepository paymentRepository;
    private Order order;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(1);

        order = new Order("order-1", List.of(product), 1708560000L, "Safira Sudrajat");
    }

    @Test
    void testSaveCreateAndFindById() {
        Payment payment = new Payment(
                "payment-1",
                order,
                "Voucher Code",
                "SUCCESS",
                Map.of("voucherCode", "ESHOP1234ABC5678")
        );

        Payment saved = paymentRepository.save(payment);
        Payment found = paymentRepository.findById("payment-1");

        assertNotNull(saved);
        assertNotNull(found);
        assertEquals("payment-1", found.getId());
    }

    @Test
    void testSaveUpdateExistingPayment() {
        Payment oldPayment = new Payment(
                "payment-1",
                order,
                "Voucher Code",
                "REJECTED",
                Map.of("voucherCode", "INVALID")
        );

        Payment newPayment = new Payment(
                "payment-1",
                order,
                "Voucher Code",
                "SUCCESS",
                Map.of("voucherCode", "ESHOP1234ABC5678")
        );

        paymentRepository.save(oldPayment);
        Payment saved = paymentRepository.save(newPayment);
        Payment found = paymentRepository.findById("payment-1");

        assertEquals("SUCCESS", saved.getStatus());
        assertEquals("SUCCESS", found.getStatus());
    }

    @Test
    void testFindByIdNotFoundReturnsNull() {
        Payment found = paymentRepository.findById("unknown");
        assertNull(found);
    }

    @Test
    void testFindAllReturnsAllSavedPayments() {
        paymentRepository.save(new Payment(
                "payment-1",
                order,
                "Voucher Code",
                "SUCCESS",
                Map.of("voucherCode", "ESHOP1234ABC5678")
        ));
        paymentRepository.save(new Payment(
                "payment-2",
                order,
                "Cash on Delivery",
                "SUCCESS",
                Map.of("address", "Jl. Merdeka", "deliveryFee", "10000")
        ));

        List<Payment> payments = paymentRepository.findAll();
        assertEquals(2, payments.size());
    }
}
