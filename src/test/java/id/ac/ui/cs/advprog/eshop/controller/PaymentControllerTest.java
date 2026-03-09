package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class PaymentControllerTest {

    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private PaymentService paymentService;

    @Mock
    private Model model;

    private Payment payment;

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Sampo");
        product.setProductQuantity(1);

        Order order = new Order("order-1", List.of(product), 1708560000L, "Safira");
        payment = new Payment(
                "payment-1",
                order,
                "Voucher Code",
                "SUCCESS",
                Map.of("voucherCode", "ESHOP1234ABC5678")
        );
    }

    @Test
    void paymentDetailFormPageShouldReturnDetailFormView() {
        String view = paymentController.paymentDetailFormPage();
        assertEquals("paymentDetailForm", view);
    }

    @Test
    void paymentDetailPageShouldSetPaymentAndReturnDetailView() {
        when(paymentService.getPayment("payment-1")).thenReturn(payment);

        String view = paymentController.paymentDetailPage("payment-1", model);

        verify(paymentService).getPayment("payment-1");
        verify(model).addAttribute("payment", payment);
        assertEquals("paymentDetail", view);
    }

    @Test
    void paymentAdminListPageShouldSetPaymentsAndReturnListView() {
        when(paymentService.getAllPayments()).thenReturn(List.of(payment));

        String view = paymentController.paymentAdminListPage(model);

        verify(paymentService).getAllPayments();
        verify(model).addAttribute("payments", List.of(payment));
        assertEquals("paymentAdminList", view);
    }

    @Test
    void paymentAdminDetailPageShouldRedirectWhenPaymentNotFound() {
        when(paymentService.getPayment("missing")).thenReturn(null);

        String view = paymentController.paymentAdminDetailPage("missing", model);

        assertEquals("redirect:/payment/admin/list", view);
    }

    @Test
    void paymentAdminDetailPageShouldSetPaymentWhenFound() {
        when(paymentService.getPayment("payment-1")).thenReturn(payment);

        String view = paymentController.paymentAdminDetailPage("payment-1", model);

        verify(model).addAttribute("payment", payment);
        assertEquals("paymentAdminDetail", view);
    }

    @Test
    void paymentAdminSetStatusShouldRedirectWhenPaymentNotFound() {
        when(paymentService.getPayment("missing")).thenReturn(null);

        String view = paymentController.paymentAdminSetStatus("missing", "SUCCESS");

        assertEquals("redirect:/payment/admin/list", view);
    }

    @Test
    void paymentAdminSetStatusShouldUpdateStatusAndRedirectToDetail() {
        when(paymentService.getPayment("payment-1")).thenReturn(payment);

        String view = paymentController.paymentAdminSetStatus("payment-1", "REJECTED");

        verify(paymentService).setStatus(payment, "REJECTED");
        assertEquals("redirect:/payment/admin/detail/payment-1", view);
    }
}
