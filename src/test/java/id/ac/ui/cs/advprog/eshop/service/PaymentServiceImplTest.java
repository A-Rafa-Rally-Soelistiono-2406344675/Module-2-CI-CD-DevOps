package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    private Order order;

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(1);

        List<Product> products = new ArrayList<>();
        products.add(product);

        order = new Order("order-1", products, 1708560000L, "Safira Sudrajat");
    }

    @Test
    void testAddPaymentVoucherCodeValidSetsSuccess() {
        Map<String, String> data = Map.of("voucherCode", "ESHOP1234ABC5678");

        Payment result = paymentService.addPayment(order, "Voucher Code", data);
        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(captor.capture());

        Payment savedPayment = captor.getValue();
        assertNotNull(savedPayment.getId());
        assertEquals("Voucher Code", savedPayment.getMethod());
        assertEquals("SUCCESS", savedPayment.getStatus());
        assertEquals("SUCCESS", result.getStatus());
    }

    @Test
    void testAddPaymentVoucherCodeInvalidSetsRejected() {
        Map<String, String> data = Map.of("voucherCode", "INVALID");

        Payment result = paymentService.addPayment(order, "Voucher Code", data);
        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testAddPaymentCodValidSetsSuccess() {
        Map<String, String> data = Map.of(
                "address", "Jl. Merdeka No. 1",
                "deliveryFee", "12000"
        );

        Payment result = paymentService.addPayment(order, "Cash on Delivery", data);
        assertEquals("SUCCESS", result.getStatus());
    }

    @Test
    void testAddPaymentCodEmptyAddressSetsRejected() {
        Map<String, String> data = Map.of(
                "address", "",
                "deliveryFee", "12000"
        );

        Payment result = paymentService.addPayment(order, "Cash on Delivery", data);
        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testSetStatusSuccessUpdatesOrderToSuccess() {
        Payment payment = new Payment(
                "payment-1",
                order,
                "Voucher Code",
                "REJECTED",
                Map.of("voucherCode", "INVALID")
        );

        Payment result = paymentService.setStatus(payment, "SUCCESS");

        assertEquals("SUCCESS", result.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());
        verify(paymentRepository).save(payment);
    }

    @Test
    void testSetStatusRejectedUpdatesOrderToFailed() {
        Payment payment = new Payment(
                "payment-1",
                order,
                "Voucher Code",
                "SUCCESS",
                Map.of("voucherCode", "ESHOP1234ABC5678")
        );

        Payment result = paymentService.setStatus(payment, "REJECTED");

        assertEquals("REJECTED", result.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), order.getStatus());
        verify(paymentRepository).save(payment);
    }

    @Test
    void testAddPaymentUnknownMethodThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                paymentService.addPayment(order, "Crypto", Map.of("wallet", "abc")));
    }

    @Test
    void testGetPaymentDelegatesToRepository() {
        Payment payment = new Payment(
                "payment-1",
                order,
                "Voucher Code",
                "SUCCESS",
                Map.of("voucherCode", "ESHOP1234ABC5678")
        );

        when(paymentRepository.findById("payment-1")).thenReturn(payment);
        Payment found = paymentService.getPayment("payment-1");

        verify(paymentRepository).findById("payment-1");
        assertEquals("payment-1", found.getId());
    }

    @Test
    void testGetAllPaymentsDelegatesToRepository() {
        List<Payment> allPayments = List.of(
                new Payment(
                        "payment-1",
                        order,
                        "Voucher Code",
                        "SUCCESS",
                        Map.of("voucherCode", "ESHOP1234ABC5678")
                )
        );

        when(paymentRepository.findAll()).thenReturn(allPayments);
        List<Payment> result = paymentService.getAllPayments();

        verify(paymentRepository).findAll();
        assertEquals(1, result.size());
    }
}
